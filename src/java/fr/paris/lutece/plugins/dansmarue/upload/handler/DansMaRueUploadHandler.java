package fr.paris.lutece.plugins.dansmarue.upload.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;

public class DansMaRueUploadHandler extends AbstractUploadHandler{
	public static final String DMR_UPLOAD_HANDLER = "DMRUploadHandler";
	
	public static final String SESSION_DEMANDE_ID = "demandeSignalement";
	
	private static Map<String, Map<String, List<FileItem>>> mapAsynchronousUpload = new ConcurrentHashMap<>();
	
	@Override
	public String getIdInSession(HttpSession session) {
		 Object numDemande = session.getAttribute(SESSION_DEMANDE_ID);
	        return numDemande != null ? numDemande.toString() : null;
	}

	@Override
	public String getUploadDirectory() {
		// TODO Auto-generated method stub
		return uploadDirectory;
	}

	@Override
	Map<String, Map<String, List<FileItem>>> getMapAsynchronousUpload() {
		// TODO Auto-generated method stub
		return mapAsynchronousUpload;
	}



}
