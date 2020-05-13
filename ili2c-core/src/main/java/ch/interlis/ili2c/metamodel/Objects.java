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


public class Objects extends Evaluable
{
  private Viewable   context;
  private Viewable   base;
	private java.util.ArrayList restrictedTo=new java.util.ArrayList();
  public Objects (Viewable context)
  {
	  this.context=context;
  }
  @Override
  public boolean isLogical() {
      return false;
  }
  public Viewable getContext()
  {
    return context;
  }
  public void addRestrictedTo(Viewable classOrAssociation)
  {
	  restrictedTo.add(classOrAssociation);
  }
  public java.util.Iterator iteratorRestrictedTo()
  {
  	return restrictedTo.iterator();
  }


public Viewable getBase() {
	return base;
}


public void setBase(Viewable base) {
	this.base = base;
}
}
