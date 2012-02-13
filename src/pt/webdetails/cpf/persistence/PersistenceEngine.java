/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.webdetails.cpf.persistence;

import com.orientechnologies.orient.core.db.document.ODatabaseDocumentPool;
import com.orientechnologies.orient.core.db.document.ODatabaseDocumentTx;
import com.orientechnologies.orient.core.exception.ODatabaseException;
import com.orientechnologies.orient.core.exception.ORecordNotFoundException;
import com.orientechnologies.orient.core.id.ORID;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import java.text.SimpleDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.orientechnologies.orient.server.OServerMain;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pentaho.platform.api.engine.IParameterProvider;
import org.pentaho.platform.api.engine.IPentahoSession;
import pt.webdetails.cpf.InvalidOperationException;
import pt.webdetails.cpf.Util;

/**
 *
 * @author pdpi
 */
public class PersistenceEngine {

    private static final Log logger = LogFactory.getLog(PersistenceEngine.class);
    private static PersistenceEngine _instance;
    private static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final String DBURL = "remote:localhost/webdetails";
    private static final String DBUSERNAME = "admin";
    private static final String DBPASSWORD = "admin";
    private static final int JSON_INDENT = 2;

    public static synchronized PersistenceEngine getInstance() {
        if (_instance == null) {
            _instance = new PersistenceEngine();
        }
        return _instance;
    }

    private enum Method {

        DELETE, GET, STORE, QUERY
    }

    private PersistenceEngine() {
        try {
            logger.info("Creating PersistenceEngine instance");
            initialize();
        } catch (Exception ex) {
            logger.fatal("Could not create PersistenceEngine: " + Util.getExceptionDescription(ex)); //$NON-NLS-1$
            return;
        }

    }

    private void initialize() throws Exception {
        System.setProperty("ORIENTDB_HOME", ".");
        OServerMain.create().startup(
                "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
                + "<orient-server>"
                + "<network>"
                + "<protocols>"
                + "<protocol name=\"binary\" implementation=\"com.orientechnologies.orient.server.network.protocol.binary.ONetworkProtocolBinary\"/>"
                + "<protocol name=\"http\" implementation=\"com.orientechnologies.orient.server.network.protocol.http.ONetworkProtocolHttpDb\"/>"
                + "</protocols>"
                + "<listeners>"
                + "<listener ip-address=\"0.0.0.0\" port-range=\"2424-2430\" protocol=\"binary\"/>"
                + "<listener ip-address=\"0.0.0.0\" port-range=\"2480-2490\" protocol=\"http\"/>"
                + "</listeners>"
                + "</network>"
                + "<storages>"
                + "<storage name=\"temp\" path=\"memory:temp\" userName=\"admin\" userPassword=\"admin\" loaded-at-startup=\"true\"/>"
                + "<storage name=\"webdetails\" path=\"local:webdetails.db\" userName=\"admin\" userPassword=\"admin\" loaded-at-startup=\"true\"/>"
                + "</storages>                "
                + "<users>"
                + "<user name=\"root\" password=\"root\" resources=\"*\"/>"
                + "</users>"
                + "<properties>"
                + "<entry name=\"orientdb.www.path\" value=\".\"/>"
                + "<entry name=\"orientdb.config.file\" value=\"orientdb-server-config.xml\"/>"
                + "<entry name=\"server.cache.staticResources\" value=\"false\"/>"
                + "<entry name=\"log.console.level\" value=\"info\"/>"
                + "<entry name=\"log.file.level\" value=\"fine\"/>"
                + "</properties>" + "</orient-server>");
    }

    public String process(IParameterProvider requestParams, IPentahoSession userSession) throws InvalidOperationException {
        String methodString = requestParams.getStringParameter("method", "none");
        JSONObject reply = null;
        try {
            Method mthd = Method.valueOf(methodString.toUpperCase());
            switch (mthd) {
                case DELETE:
                    reply = delete(requestParams, userSession);
                    break;
                case GET:
                    reply = get(requestParams, userSession);
                    break;
                case QUERY:
                    reply = query(requestParams, userSession);
                    break;
                case STORE:
                    reply = store(requestParams, userSession);
                    break;
            }

            return reply.toString(JSON_INDENT);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid method: " + methodString);
            return "{'result': false}";
        } catch (JSONException e) {
            logger.error("error processing: " + methodString);
            return "{'result': false}";
        }
    }

    public JSONObject query(String query) throws JSONException {
        JSONObject json = new JSONObject();
        try {
            json.put("result", Boolean.TRUE);

            ODatabaseDocumentTx db = ODatabaseDocumentPool.global().acquire(DBURL, DBUSERNAME, DBPASSWORD);


            List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>(query));
            JSONArray arr = new JSONArray();
            for (ODocument resDoc : result) {
                arr.put(new JSONObject(resDoc.toJSON()));
            }

            json.put("object", arr);
        } catch (ODatabaseException ode) {
            json.put("result", Boolean.FALSE);
            json.put("errorMessage", "DatabaseException: Review query");
            logger.error(getExceptionDescription(ode));

        }

        return json;

    }

    private JSONObject query(IParameterProvider requestParams, IPentahoSession userSession) throws JSONException {
        return query(requestParams.getStringParameter("query", ""));
    }

    private JSONObject get(IParameterProvider requestParams, IPentahoSession userSession) throws JSONException {
        final String id = requestParams.getStringParameter("rid", "");
        return get(id);
    }

    public JSONObject get(String id) throws JSONException {

        JSONObject json = new JSONObject();

        try {
            json.put("result", Boolean.TRUE);

            ODatabaseDocumentTx db = ODatabaseDocumentPool.global().acquire(DBURL, DBUSERNAME, DBPASSWORD);


            List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>("select FROM " + id));
            ODocument doc;

            if (result.size() == 1) {
                doc = result.get(0);
            } else {
                json.put("result", Boolean.FALSE);
                json.put("errorMessage", "Multiple elements found with id " + id);
                return json;
            }

            JSONObject resultDoc = new JSONObject(doc.toJSON());

            json.put("object", resultDoc);


        } catch (ODatabaseException orne) {

            if (orne.getCause().getClass() == ORecordNotFoundException.class) {
                logger.error("Record with id " + id + " not found");
                json.put("result", Boolean.FALSE);
                json.put("errorMessage", "No record found with id " + id);
            } else {
                logger.error(getExceptionDescription(orne));
                throw orne;
            }
        }

        return json;
    }

    private JSONObject delete(IParameterProvider requestParams, IPentahoSession userSession) throws JSONException {
        final String id = requestParams.getStringParameter("rid", "");
        return delete(id);
    }

    public JSONObject delete(String id) throws JSONException {


        JSONObject json = new JSONObject();

        try {
            json.put("result", Boolean.TRUE);

            ODatabaseDocumentTx db = ODatabaseDocumentPool.global().acquire(DBURL, DBUSERNAME, DBPASSWORD);


            List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>("select FROM " + id));
            ODocument doc;

            if (result.size() == 1) {
                doc = result.get(0);
            } else if (result.size() == 0) {
                json.put("result", Boolean.FALSE);
                json.put("errorMessage", "No element found with id " + id);
                return json;
            } else {
                json.put("result", Boolean.FALSE);
                json.put("errorMessage", "Multiple elements found with id " + id);
                return json;
            }


            doc.delete();
        } catch (ODatabaseException orne) {

            if (orne.getCause().getClass() == ORecordNotFoundException.class) {
                logger.error("Record with id " + id + " not found");
                json.put("result", Boolean.FALSE);
                json.put("errorMessage", "No record found with id " + id);
            } else {
                logger.error(getExceptionDescription(orne));
                throw orne;
            }
        }
        return json;
    }

    private JSONObject store(IParameterProvider requestParams, IPentahoSession userSession) throws JSONException {
        final String id = requestParams.getStringParameter("rid", "");
        String className = requestParams.getStringParameter("class", "");
        String data = requestParams.getStringParameter("data", "");
        return store(id, className, data);
    }

    public JSONObject store(String id, String className, String inputData) throws JSONException {
        JSONObject json = new JSONObject();
        try {

            json.put("result", Boolean.TRUE);

            JSONObject data = new JSONObject(inputData);
            ODatabaseDocumentTx db = ODatabaseDocumentPool.global().acquire(DBURL, DBUSERNAME, DBPASSWORD);
            ODocument doc;

            if (id == null || id.length() == 0) {
                doc = new ODocument(db, className);
            } else {
                List<ODocument> result = db.query(new OSQLSynchQuery<ODocument>("select FROM " + id));

                if (result.size() == 1) {
                    doc = result.get(0);
                } else if (result.size() == 0) {
                    json.put("result", Boolean.FALSE);
                    json.put("errorMessage", "No " + className + " found with id " + id);
                    return json;
                } else {
                    json.put("result", Boolean.FALSE);
                    json.put("errorMessage", "Multiple " + className + " found with id " + id);
                    return json;
                }
            }

            // CREATE A NEW DOCUMENT AND FILL IT
            Iterator keyIterator = data.keys();
            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                doc.field(key, data.getString(key));
            }

            // SAVE THE DOCUMENT
            doc.save();

            if (id == null || id.length() == 0) {
                ORID newId = doc.getIdentity();
                json.put("id", newId.toString());
            }

            db.close();

            return json;
        } catch (ODatabaseException orne) {

            if (orne.getCause().getClass() == ORecordNotFoundException.class) {
                logger.error("Record with id " + id + " not found");
                json.put("result", Boolean.FALSE);
                json.put("errorMessage", "No " + className + " found with id " + id);
                return json;
            }

            logger.error(getExceptionDescription(orne));
            throw orne;
        }
    }

    private String getExceptionDescription(Exception ex) {
        return ex.getCause().getClass().getName() + " - " + ex.getMessage();
    }
}
