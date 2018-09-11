-- Mise à jour des modèles de mails pour les notification_3_contents des actions "Déclarer les services Faits" 
UPDATE signalement_workflow_notifuser_3contents_config SET 
                message1 = '<p>Bonjour,</p>
                               <p>L’anomalie " ${type}" que vous avez signalée au "${adresse}" a été traitée.
                               </p>
                               <p>Les services municpaux n’ont pas constaté de dépôt le ${datetraitement} à ${heuretraitement}
                               </p>
                               <p>Votre demande est clôturée.
                               </p>
                               <p>Nous vous remercions une nouvelle fois de votre participation qui a permis d’améliorer la qualité de notre environnement urbain.
                               </p>
                               <p>Les équipes de la Mairie de Paris en charge de l’espace public.
                               </p>'
                ,message2 = '<p>Bonjour,
                               </p>
                               <p>L’anomalie " ${type}" que vous avez signalée au "${adresse}"&nbsp; a été traitée. Les services municipaux ont opéré les actions correctrices appropriées.
                               </p>
                               <p>Votre demande est clôturée.
                               </p>
                               <p>Nous vous remercions une nouvelle fois de votre participation qui a permis d’améliorer la qualité de notre environnement urbain.
							   </p>
                               <p>Les équipes de la Mairie de Paris en charge de l’espace public.
                               </p>'
                ,message3 = '<p>Bonjour,
							</p>
                            <p>L’anomalie " ${type}" que vous avez signalée au "${adresse}" a fait l’objet d’une analyse approfondie par les services compétents afin d’assurer la sécurité du site en tant que de besoin. Sa résolution complète nécessite des travaux qui ne peuvent être engagés dans l’immédiat. L’anomalie est cependant enregistrée et fera l’objet d’une surveillance jusqu’à la planification de son traitement définitif.
							</p>	
							<p>Votre demande est clôturée.</p>
							<p>Nous vous remercions une nouvelle fois de votre participation qui a permis d’améliorer la qualité de notre environnement urbain.
							</p>
                            <p>Les équipes de la Mairie de Paris en charge de l’espace public.
                            </p>'

WHERE id_task IN (85,86,87,88,89,95);