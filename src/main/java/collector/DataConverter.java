package collector;

import java.util.ArrayList;

import com.google.gson.Gson;

import org.bson.Document;

/**
 * Class that converts java objects to bson documents that can be read by MongoDB.
 */

class DataConverter {

    /**
     * Method that does the conversion mentioned in the class javadoc.
     * @param data - List containing DataObject instances that will be converted to MongoDB documents.
     * @return Tme same list of objects, now in the Document format that can directly be inserted into MongoDB.
     */
    protected static ArrayList<Document> convertDataObjectToDocument(ArrayList<DataObject> data) {
        ArrayList<Document> dataDocs = new ArrayList<Document>();
        for (DataObject dataInstance : data){
            Gson gson = new Gson();
            // Transform object to JSON.
            String json = gson.toJson(dataInstance);
            // Transform JSON to document.
            Document doc = Document.parse(json);
            dataDocs.add(doc);
        }

        return dataDocs;
    }
}
