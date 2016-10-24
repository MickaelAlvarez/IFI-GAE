package guestbook;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import com.google.appengine.api.datastore.*;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class BookServlet extends HttpServlet {
	public static String ENTITY_BOOK = "Book";
	public static String ANCESTOR_KIND = "BookHistory";
	public static String ANCESTOR_NAME = "book";
	public static String PROP_TITLE = "title";
	public static String PROP_AUTOR = "autor";
	public static String PROP_DESC = "description";
	public static String PROP_DATE = "date";
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			// Demande tous les 5 derniers messages triés par date décroissante
			Query q = new Query(ENTITY_BOOK).setAncestor(KeyFactory.createKey(ANCESTOR_KIND, ANCESTOR_NAME)).addSort("date",
					SortDirection.DESCENDING);
			List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));

			req.setAttribute(ENTITY_BOOK, results);
			this.getServletContext().getRequestDispatcher("/WEB-INF/guestbook.jsp").forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

			// Stocke le message posté
			Entity message = new Entity(ENTITY_BOOK, KeyFactory.createKey(ANCESTOR_KIND, ANCESTOR_NAME));
			message.setProperty(PROP_TITLE, req.getParameter(PROP_TITLE));
			message.setProperty(PROP_AUTOR, req.getParameter(PROP_AUTOR));
			message.setProperty(PROP_DESC, req.getParameter(PROP_DESC));
			message.setProperty(PROP_DATE, new Date());

			datastore.put(message);

			resp.sendRedirect("/");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}