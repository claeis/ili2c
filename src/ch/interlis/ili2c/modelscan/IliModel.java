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
 * @version $Revision: 1.2 $ $Date: 2007-03-07 08:36:08 $
 */
public class IliModel {
	private String name=null;
	private HashSet depv=new HashSet();

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

}
