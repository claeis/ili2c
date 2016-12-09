/* This file is part of the ili2c project.
 * For more information, please see <http://www.interlis.ch>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package ch.interlis.ili2c.config;

import java.io.*;

public class PersistenceService {

  private static final String AUTO_COMPLETE_MODELS = "--auto-complete-models";
static public Configuration readConfig(String filename)
    throws IOException, FileNotFoundException
  {
        File inputFile = new File(filename);
        BufferedReader in = new BufferedReader(new FileReader(inputFile));

        Configuration config=new Configuration();
        config.setCheckMetaObjs(false);

        String line;
        while ((line = in.readLine()) != null){
           String arg=line.trim();
           if(arg.startsWith("-")){
             if(arg.startsWith("-m ")){
                String optvalue=arg.substring(3);
                FileEntry entry=new FileEntry(optvalue,FileEntryKind.METADATAFILE);
                config.addFileEntry(entry);
             }else if(arg.startsWith("-o0")){
              config.setOutputKind(GenerateOutputKind.NOOUTPUT);
             }else if(arg.startsWith("-o1 ")){
              String optvalue=arg.substring(4);
              config.setOutputFile(optvalue);
              config.setOutputKind(GenerateOutputKind.ILI1);
             }else if(arg.startsWith("-o2 ")){
              String optvalue=arg.substring(4);
              config.setOutputFile(optvalue);
              config.setOutputKind(GenerateOutputKind.ILI2);
             }else if(arg.startsWith("-oXSD ")){
              String optvalue=arg.substring(6);
              config.setOutputFile(optvalue);
              config.setOutputKind(GenerateOutputKind.XMLSCHEMA);
             }else if(arg.startsWith("-oFMT ")){
              String optvalue=arg.substring(6);
              config.setOutputFile(optvalue);
              config.setOutputKind(GenerateOutputKind.ILI1FMTDESC);
             }else if(arg.startsWith("-oJAVA ")){
             	// ignore it
				//String optvalue=arg.substring(7);
				//config.setOutputFile(optvalue);
              	//config.setOutputKind(GenerateOutputKind.JAVA);
			 }else if(arg.startsWith("-oGML ")){
				String optvalue=arg.substring(8);
				config.setOutputFile(optvalue);
			  config.setOutputKind(GenerateOutputKind.GML32);
			 }else if(arg.startsWith("-oILIGML2 ")){
				String optvalue=arg.substring(12);
				config.setOutputFile(optvalue);
			  config.setOutputKind(GenerateOutputKind.ILIGML2);
			 }else if(arg.startsWith("-oETF1 ")){
					String optvalue=arg.substring(9);
					config.setOutputFile(optvalue);
				  config.setOutputKind(GenerateOutputKind.ETF1);
			 }else if(arg.startsWith("-oIMD ")){
					String optvalue=arg.substring(8);
					config.setOutputFile(optvalue);
				  config.setOutputKind(GenerateOutputKind.IMD);
			 }else if(arg.startsWith("-oIMD16 ")){
					String optvalue=arg.substring(8);
					config.setOutputFile(optvalue);
				  config.setOutputKind(GenerateOutputKind.IMD16);
			 }else if(arg.startsWith("-oUML ")){
					String optvalue=arg.substring(8);
					config.setOutputFile(optvalue);
				  config.setOutputKind(GenerateOutputKind.UML21);
			 }else if(arg.startsWith("-oIOM ")){
	             	// ignore it
				//String optvalue=arg.substring(8);
				//config.setOutputFile(optvalue);
			    //config.setOutputKind(GenerateOutputKind.IOM);
             }else if(arg.startsWith("-boid ")){
              String optvalue=arg.substring(6);
              int eqpos=optvalue.indexOf('=');
              //if(eqpos==-1){
    	      //    System.err.println (progName + ":after -boid is an argument of the form basketName=boid required; but found " + args[i]);
    	      //    continue;
              //}
              String qualifiedBasketName=optvalue.substring(0,eqpos);
              String boid=optvalue.substring(eqpos+1);
              config.addBoidEntry(new BoidEntry(qualifiedBasketName,boid));
             }else if(arg.startsWith("--with-predefined")){
	             	// ignore it
              //config.setIncPredefModel(true);
             }else if(arg.startsWith("--check-metaobj")){
              config.setCheckMetaObjs(true);
             }else if(arg.startsWith(AUTO_COMPLETE_MODELS)){
                 config.setAutoCompleteModelList(true);
             }else if(arg.startsWith("--without-warnings")){
              config.setGenerateWarnings(false);
             }else{
              // ignore line
             }
           }else if(arg.length()>0){
                FileEntry entry=new FileEntry(arg,FileEntryKind.ILIMODELFILE);
                config.addFileEntry(entry);
           }else{
            // ignore empty line
           }
        }
        in.close();
        return config;
  }
  static public void writeConfig(String filename,Configuration config)
    throws IOException //, FileNotFoundException
  {
        File outputFile = new File(filename);
        BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));

        // general options
        //if(config.isIncPredefModel()){
        //  out.write("--with-predefined");
        //  out.newLine();
        //}
        if(!config.isGenerateWarnings()){
          out.write("--without-warnings");
          out.newLine();
        }
        if(config.isCheckMetaObjs()){
          out.write("--check-metaobj");
          out.newLine();
        }
        if(config.isAutoCompleteModelList()){
            out.write(AUTO_COMPLETE_MODELS);
            out.newLine();
          }

        // boid  to basket mappings
        java.util.Iterator boidi=config.iteratorBoidEntry();
        while(boidi.hasNext()){
          BoidEntry e=(BoidEntry)boidi.next();
          out.write("-boid ");
          out.write(e.getMetaDataUseDef());
          out.write("=");
          out.write(e.getBoid());
          out.newLine();
        }

        // model and metadata files
        java.util.Iterator filei=config.iteratorFileEntry();
        while(filei.hasNext()){
          FileEntry e=(FileEntry)filei.next();
          if(e.getKind()==FileEntryKind.METADATAFILE){
            out.write("-m ");
          }
          out.write(e.getFilename());
          out.newLine();
        }

        // output options
        switch(config.getOutputKind()){
          case GenerateOutputKind.NOOUTPUT:
            out.write("-o0");
            break;
          case GenerateOutputKind.ILI1:
            out.write("-o1 ");
            out.write(config.getOutputFile());
            break;
          case GenerateOutputKind.ILI2:
            out.write("-o2 ");
            out.write(config.getOutputFile());
            break;
          case GenerateOutputKind.XMLSCHEMA:
            out.write("-oXSD ");
            out.write(config.getOutputFile());
            break;
          case GenerateOutputKind.ILI1FMTDESC:
            out.write("-oFMT ");
            out.write(config.getOutputFile());
            break;
		  case GenerateOutputKind.GML32:
			out.write("-oGML ");
			out.write(config.getOutputFile());
			  break;
		  case GenerateOutputKind.ILIGML2:
			out.write("-oILIGML2 ");
			out.write(config.getOutputFile());
			  break;
		  case GenerateOutputKind.ETF1:
				out.write("-oETF1 ");
				out.write(config.getOutputFile());
				  break;
		  case GenerateOutputKind.IMD:
				out.write("-oIMD ");
				out.write(config.getOutputFile());
				  break;
		  case GenerateOutputKind.IMD16:
				out.write("-oIMD16 ");
				out.write(config.getOutputFile());
				  break;
		  case GenerateOutputKind.UML21:
				out.write("-oUML ");
				out.write(config.getOutputFile());
				  break;
          default:
            // ignore
            ;
        }
        out.newLine();

        out.close();
  }
}