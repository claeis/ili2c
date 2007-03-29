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
package ch.interlis.ili2c.modelscan;

import java.util.HashSet;

/**
 * @author ce
 * @version $Revision: 1.3 $ $Date: 2007-03-29 15:36:02 $
 */
public class IliModel {
	private String name=null;
	private HashSet depv=new HashSet();
	private double version=0.0;

	public HashSet getDependencies() {
		return depv;
	}
	public void addDepenedency(String modelName) {
		depv.add(modelName);
	}
	public String getName() {
		return name;
	}
	public void setName(String string) {
		name = string;
	}
	public double getIliVersion() {
		return version;
	}
	public void setIliVersion(double d) {
		version = d;
	}

}
