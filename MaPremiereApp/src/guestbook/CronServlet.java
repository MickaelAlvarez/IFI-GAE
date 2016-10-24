package guestbook;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class CronServlet extends HttpServlet {
	private static String TO = ""; // Entrer son adresse
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {
			DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
			Query q = new Query(BookServlet.ENTITY_BOOK).setAncestor(KeyFactory.createKey(BookServlet.ANCESTOR_KIND, BookServlet.ANCESTOR_NAME)).addSort("date",
					SortDirection.DESCENDING);
			List<Entity> results = datastore.prepare(q).asList(FetchOptions.Builder.withLimit(5));
			
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			String message = "Voici les 5 derniers livres insérés :\n";
			for(Entity e : results) {
				message += e.getProperty("title") + " de " + e.getProperty("autor") + "\n\t" + e.getProperty("description") + "\n";
			}
			
			Message msg = new MimeMessage(session);

			msg.setFrom(new InternetAddress("admin@crested-photon-147217.appspotmail.com", "Administrateur"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(TO, "MrMouette"));
			msg.setSubject("Sujet du message");
			msg.setText(message);
			Transport.send(msg);
			this.getServletContext().getRequestDispatcher("/WEB-INF/guestbook.jsp").forward(req, resp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}