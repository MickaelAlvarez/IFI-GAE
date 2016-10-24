<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.google.appengine.api.datastore.*" %>
<%@ page import="java.util.List" %>


<!DOCTYPE html>

<html>
	<head>
		<title>Gestionnaire de livre</title>
		<meta charset="utf-8" />
	</head>

	<body>
		<h1>Ajouter un livre</h1>
		<form action="/post" method="post">
			<p>
				<label>Titre : <input type="text" name="title" /></label>
			</p>
			<p>
				<label>Auteur : <input type="text" name="autor" /></label>
			</p>
			<p>
				<label>Description :<textarea name="description" style="width: 200px; height: 100px;"></textarea></label>
			</p>
			<p>
				<input type="submit" />
			</p>
		</form>

		<h1>Les dernières entrées :</h1>
		<%
			List<Entity> books = (List<Entity>) request.getAttribute("Book");
			for (Entity book : books) {
		%>
		<p>
			<strong><%= book.getProperty("title") %></strong>, écrit par <%= book.getProperty("autor") %>
		</p>
		<p>
			<em><%= book.getProperty("description") %></em>
		</p>
		<%
			}
		%>
	</body>
</html>