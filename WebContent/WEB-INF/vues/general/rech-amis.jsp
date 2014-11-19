<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>Rechercher de nouveaux amis</h2>

<div id="form-rechercher-amis">
	<!-- Formulaire de recherche d'amis -->
	<form method="get" action="${pageContext.request.contextPath}/rech-amis">
		<p>
			<label for="nom-ami">Nom : </label>
			<input type="text" name="nom-ami" id="nom-ami" />

			<label for="ville-origine">Ville d'origine : </label>
			<input type="text" name="ville-origine" id="ville-origine" />

			<label for="ville-actuelle">Ville actuelle : </label>
			<input type="text" name="ville-actuelle" id="ville-actuelle" />

			<label for="sexe-m">Masculin </label>
			<input type="checkbox" name="sexe" id="sexe-m" value="M" />

			<label for="sexe-f">Féminin </label>
			<input type="checkbox" name="sexe" id="sexe-f" value="F" />

			<input type="image" name="rech-amis" id="img-soumettre-rech-amis" src="${pageContext.request.contextPath}/images/icone-recherche.png" alt="Rechercher des amis" />
		</p>
	</form>
</div>  <!-- Fin de la division "form-rechercher-amis" -->

<div id="amis-trouves">
</div>
<img src="${pageContext.request.contextPath}/images/en-construction.png" id="img-en-construction" alt="Section en construction" />
