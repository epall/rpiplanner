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
