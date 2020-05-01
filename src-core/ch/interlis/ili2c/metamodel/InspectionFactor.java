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


public class InspectionFactor extends Evaluable
{
	private ObjectPath restriction=null; // may be null
  private  DecompositionView inspectionViewable=null;
  // XOR
  private ViewableAlias renamedViewable=null; // only valid if inspectionViewable==null
  private ObjectPath decomposedAttribute=null; // only valid if inspectionViewable==null
  private boolean areaInspection=false; // only valid if inspectionViewable==null
  public InspectionFactor ()
  {
	  
  }
  @Override
  public boolean isLogical() {
      return false;
  }
  
public boolean isAreaInspection() {
	return areaInspection;
}
public void setAreaInspection(boolean areaInspection) {
	this.areaInspection = areaInspection;
}
public ObjectPath getDecomposedAttribute() {
	return decomposedAttribute;
}
public void setDecomposedAttribute(ObjectPath decomposedAttribute) {
	this.decomposedAttribute = decomposedAttribute;
}
public DecompositionView getInspectionViewable() {
	return inspectionViewable;
}
public void setInspectionViewable(DecompositionView inspectionViewable) {
	this.inspectionViewable = inspectionViewable;
}
public ViewableAlias getRenamedViewable() {
	return renamedViewable;
}
public void setRenamedViewable(ViewableAlias renamedViewable) {
	this.renamedViewable = renamedViewable;
}
public ObjectPath getRestriction() {
	return restriction;
}
public void setRestriction(ObjectPath retriction) {
	this.restriction = retriction;
}
}
