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
package ch.interlis.ili2c.metamodel;

/** Semantic errors detected by the metamodel.
 * @author ce
 * @version $Revision: 1.2 $ $Date: 2007-12-06 10:57:27 $
 */
public class Ili2cSemanticException extends RuntimeException {
    private static final long serialVersionUID = 6210019945556647248L;

    private int sourceLine=0;

    public Ili2cSemanticException() {
		super();
	}
	public Ili2cSemanticException(String message) {
		super(message);
	}
	public Ili2cSemanticException(Throwable cause) {
		super(cause);
	}
	public Ili2cSemanticException(String message, Throwable cause) {
		super(message, cause);
	}
	public Ili2cSemanticException(int sourceLine) {
		super();
		this.sourceLine=sourceLine;
	}
	public Ili2cSemanticException(int sourceLine,String message) {
		super(message);
		this.sourceLine=sourceLine;
	}
	public Ili2cSemanticException(int sourceLine,Throwable cause) {
		super(cause);
		this.sourceLine=sourceLine;
	}
	public Ili2cSemanticException(int sourceLine,String message, Throwable cause) {
		super(message, cause);
		this.sourceLine=sourceLine;
	}
	public int getSourceLine() {
		return sourceLine;
	}

}
