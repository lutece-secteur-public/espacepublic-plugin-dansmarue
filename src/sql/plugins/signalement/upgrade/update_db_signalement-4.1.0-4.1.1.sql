-- Mise � jour des mod�les de mails pour les notification_3_contents des actions "D�clarer les services Faits" 
UPDATE signalement_workflow_notifuser_3contents_config SET 
                message1 = '<p>Bonjour,</p>
                               <p>L�anomalie " ${type}" que vous avez signal�e au "${adresse}" a �t� trait�e.
                               </p>
                               <p>Les services municpaux n�ont pas constat� de d�p�t le ${datetraitement} � ${heuretraitement}
                               </p>
                               <p>Votre demande est cl�tur�e.
                               </p>
                               <p>Nous vous remercions une nouvelle fois de votre participation qui a permis d�am�liorer la qualit� de notre environnement urbain.
                               </p>
                               <p>Les �quipes de la Mairie de Paris en charge de l�espace public.
                               </p>'
                ,message2 = '<p>Bonjour,
                               </p>
                               <p>L�anomalie " ${type}" que vous avez signal�e au "${adresse}"&nbsp; a �t� trait�e. Les services municipaux ont op�r� les actions correctrices appropri�es.
                               </p>
                               <p>Votre demande est cl�tur�e.
                               </p>
                               <p>Nous vous remercions une nouvelle fois de votre participation qui a permis d�am�liorer la qualit� de notre environnement urbain.
							   </p>
                               <p>Les �quipes de la Mairie de Paris en charge de l�espace public.
                               </p>'
                ,message3 = '<p>Bonjour,
							</p>
                            <p>L�anomalie " ${type}" que vous avez signal�e au "${adresse}" a fait l�objet d�une analyse approfondie par les services comp�tents afin d�assurer la s�curit� du site en tant que de besoin. Sa r�solution compl�te n�cessite des travaux qui ne peuvent �tre engag�s dans l�imm�diat. L�anomalie est cependant enregistr�e et fera l�objet d�une surveillance jusqu�� la planification de son traitement d�finitif.
							</p>	
							<p>Votre demande est cl�tur�e.</p>
							<p>Nous vous remercions une nouvelle fois de votre participation qui a permis d�am�liorer la qualit� de notre environnement urbain.
							</p>
                            <p>Les �quipes de la Mairie de Paris en charge de l�espace public.
                            </p>'

WHERE id_task IN (85,86,87,88,89,95);