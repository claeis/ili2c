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


/** An alias for a Viewable. For instance, <code>JOIN VIEW</code>s
    allow to "locally rename" (i.e. make an alias) the joined viewables.
    The same Viewable can be given several aliases. Thus, the same
    viewable can be joined several times.
*/
public class ViewableAlias extends Evaluable
{
  private Viewable   aliasing;
  private String     name;
  private boolean includeNull=false;

  /** Creates a new ViewableAlias for a given viewable.

      @param name The alias name (the part before the tilde),
                  or <code>null</code> if no special alias name
                  is used to rename a Viewable.

      @param aliasing The Viewable being aliased.
  */
  public ViewableAlias (String name, Viewable aliasing)
  {
    this.aliasing = aliasing;
    this.name = name;
  }
  
  @Override
  public boolean isLogical() {
      return false;
  }

  /** Returns the alias name, or the name of the aliased Viewable.
  */
  public String getName ()
  {
    return name!=null ? name : aliasing.getName();
  }


  /** Returns the Viewable for which this object is an alias for. */
  public Viewable getAliasing ()
  {
    return aliasing;
  }

  /** include empty object into join, if no matching one defined */
  public void setIncludeNull(boolean v){
	  includeNull=v;
  }
public boolean isIncludeNull() {
	return includeNull;
}
}
