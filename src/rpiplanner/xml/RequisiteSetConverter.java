/*
 * Copyright (C) 2008 Eric Allen allene2@rpi.edu
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */

package rpiplanner.xml;

import rpiplanner.model.Course;
import rpiplanner.model.RequisiteSet;

import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.collections.AbstractCollectionConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class RequisiteSetConverter extends AbstractCollectionConverter {
	public RequisiteSetConverter(Mapper mapper) {
		super(mapper);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean canConvert(Class clazz) {
		return clazz.equals(RequisiteSet.class);
	}

	@Override
	public void marshal(Object set, HierarchicalStreamWriter writer,
			MarshallingContext context) {
		RequisiteSet rset = (RequisiteSet)set;
		writer.addAttribute("required", String.valueOf(rset.isRequired()));
		writer.addAttribute("pickOne", String.valueOf(rset.isPickOne()));
		for(Course c : rset){
			writeItem(c, context, writer);
		}
	}

	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
			UnmarshallingContext context) {
		RequisiteSet rset = new RequisiteSet();
		rset.setRequired(Boolean.valueOf(reader.getAttribute("required")));
		rset.setPickOne(Boolean.valueOf(reader.getAttribute("pickOne")));
		while(reader.hasMoreChildren()){
			reader.moveDown();
			rset.add((Course)readItem(reader, context, rset));
			reader.moveUp();
		}
		return rset;
	}

}
