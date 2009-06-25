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

package rpiplanner.validation.xml;

import com.thoughtworks.xstream.XStream;
import rpiplanner.model.DegreeDatabase;
import rpiplanner.model.Course;

import rpiplanner.validation.degree.Degree;
import rpiplanner.validation.requirements.*;

import java.io.InputStream;
import java.io.File;


public class DegreeXML {
    private static XStream databaseReader;

    private static XStream getDatabaseReader(){
        if(databaseReader == null){
            databaseReader = new XStream();
            initializeXStream(databaseReader);
        }
        return databaseReader;
    }

     private static void initializeXStream(XStream xs){
        xs.processAnnotations(Degree.class);
        xs.processAnnotations(CoreRequirement.class);
        xs.processAnnotations(RestrictedRequirement.class);
        xs.processAnnotations(SubjectRequirement.class);
        xs.processAnnotations(FreeElectiveRequirement.class);
        xs.processAnnotations(HumanitiesRequirement.class);
        xs.processAnnotations(Course.class);
    }


    DegreeDatabase getDegreeDatabase(InputStream in) {
        return (DegreeDatabase) getDatabaseReader().fromXML(in);
    }
}
