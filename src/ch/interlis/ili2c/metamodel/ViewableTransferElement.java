/* This file is part of the ili2c-2.6.x project.
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

/**
 * @author ce
 * @version $Revision: 1.2 $ $Date: 2007-03-07 08:36:08 $
 */
public class ViewableTransferElement {
		public ViewableTransferElement(Object obj) {this.obj=obj;}
		public ViewableTransferElement(Object obj,boolean embedded) {this.obj=obj;this.embedded=embedded;}
		public Object obj=null; // AttributeDef or RoleDef
		public boolean embedded=false; // true if embedded RoleDef (defined in a lightweight association)
}
