import os.path,shutil,sys,string,os,fnmatch

class GlobDirectoryWalker:
    # a forward iterator that traverses a directory tree

    def __init__(self, directory, pattern="*"):
        self.stack = [directory]
        self.pattern = pattern
        self.files = []
        self.index = 0

    def __getitem__(self, index):
        while 1:
            try:
                file = self.files[self.index]
                self.index = self.index + 1
            except IndexError:
                # pop next directory from stack
                self.directory = self.stack.pop()
                self.files = os.listdir(self.directory)
                self.index = 0
            else:
                # got a filename
                fullname = os.path.join(self.directory, file)
                if os.path.isdir(fullname) and not os.path.islink(fullname):
                    self.stack.append(fullname)
                if fnmatch.fnmatch(file, self.pattern):
                    return fullname


def copytree(src, dst, symlinks=0):
    names = os.listdir(src)
    if not os.path.exists(dst):
        os.mkdir(dst)
    for name in names:
        srcname = os.path.join(src, name)
        dstname = os.path.join(dst, name)
        try:
            if symlinks and os.path.islink(srcname):
                linkto = os.readlink(srcname)
                os.symlink(linkto, dstname)
            elif os.path.isdir(srcname):
                copytree(srcname, dstname)
            else:
                shutil.copy2(srcname, dstname)
            # XXX What about devices, sockets etc.?
        except (IOError, os.error), why:
            print "Can't copy %s to %s: %s" % (`srcname`, `dstname`, str(why))

def main():
	if len(sys.argv)!=4:
		print "ili2tst.py pathToReadme.txt pathToSandbox Ili2cJar" 
	else:
		if not sys.argv[1].endswith("readme.txt"):
			cases=[]
			for i in GlobDirectoryWalker(".", sys.argv[1]):
				cases.append(i)
			if len(cases)==0:
				print "no testcase "+sys.argv[1]
				return
			print os.path.abspath(cases[0])
			pathToReadme=os.path.join(os.path.abspath(cases[0]),"readme.txt")
		else:
			pathToReadme=os.path.abspath(sys.argv[1])
		pathToSandbox=os.path.abspath(sys.argv[2])
		ili2cJar=os.path.abspath(sys.argv[3])
		testpath=os.path.dirname(pathToReadme)
		testcase=os.path.basename(testpath)
		pathToSandboxCase=os.path.join(pathToSandbox,testcase)
		ili2cArgs=""
		ili2cArgsFile=os.path.join(testpath,"ili2cargs.txt")
		if os.path.exists(ili2cArgsFile):
			f=open(ili2cArgsFile,"r")
			ili2cArgs=string.strip(f.readline())
			f.close()
		testDescFile=os.path.join(testpath,"readme.txt")
		f=open(testDescFile,"r")
		testDesc=string.strip(f.readline())
		f.close()
		inputPath=os.path.join(testpath,"input")
		if os.path.exists(inputPath):
			copytree(inputPath,pathToSandboxCase)
		else:
			os.makedirs(pathToSandboxCase)
		os.chdir(pathToSandboxCase)
		testOk=os.system("java -jar "+ili2cJar+" "+ili2cArgs+" 1>stdout.txt 2>stderr.txt")==0
		if testOk:
			testOk=os.system("fc stdout.txt \""+os.path.join(testpath,"output","stdout.txt")+"\" 1>diffout.txt")==0
		if testOk:
			testOk=os.system("fc stderr.txt \""+os.path.join(testpath,"output","stderr.txt")+"\" 1>differr.txt")==0
		if testOk:
			f=open(os.path.join(pathToSandbox,"ok.txt"),"a")
			f.write(testcase+" "+testDesc+"\n")
			f.close()
		else:				
			f=open(os.path.join(pathToSandbox,"failed.txt"),"a")
			f.write(testcase+" "+testDesc+"\n")
			f.close()
			print testcase+" failed ("+testDesc+")"
		
if __name__ == "__main__":
    main()

