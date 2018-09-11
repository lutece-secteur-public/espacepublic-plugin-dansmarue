#!/bin/bash
path=/tmp/img;
log=$path/compressionImg.log;
rm -f $path/*;
mkdir $path;
touch $log;

echo "Lancement du batch "`date '+%Y-%m-%d %H:%M:%S'`>>$log;


#Récupération des parametres
file="./script.properties"
maxSize=$(grep -i 'maxSize' $file  | cut -f2 -d'=');
ko="ko";

#Taille des photos en base 
tailleInit=`psql -t -U postgres_app -d R57 -c "SELECT sum(octet_length(image_content)/1000000) taille_initiale FROM signalement_photo;"`
echo "Poid initial des photos en base:"$tailleInit "MO">>$log;

#Création procédure de conversion lo->bytea
psql -U postgres_app -d R57 -c "create or replace function bytea_import(p_path text, p_result out bytea) 
                   language plpgsql as \$\$
declare
  l_oid oid;
  r record;
begin
  p_result := '';
  select lo_import(p_path) into l_oid;
  for r in ( select data 
             from pg_largeobject 
             where loid = l_oid 
             order by pageno ) loop
    p_result = p_result || r.data;
  end loop;
  perform lo_unlink(l_oid);
end;\$\$;"

#Suppression de la table
psql -U postgres_app -d R57 -c "DROP TABLE IF EXISTS tmp_object_lo;"

#Clear les lo
psql -U postgres_app -d R57 -c "SELECT lo_unlink(l.oid) FROM pg_largeobject_metadata l;"

#Création table temporaire contenant les lo
psql -U postgres_app -d R57 -c "create table tmp_object_lo (name text,raster oid);"

#Création dossier
mkdir -p /tmp/img;


#Récupération photo avec taille >300ko
ids=`psql -t -U postgres_app -d R57 -c "SELECT id_photo FROM signalement_photo where octet_length(image_content)/1000>"$maxSize";"`

ARRAY=()
for word in $ids
do
    ARRAY+=($word) 
done

for i in "${ARRAY[@]}"
do
	echo $i;
	echo "Traitement de la photo "$i>>$log;
	#Conversion en lo
	psql -U postgres_app -d R57 -c "insert into tmp_object_lo (name, raster) values ('$i', lo_from_bytea($i,(select image_content FROM signalement_photo WHERE id_photo = $i)));"

	#export img
	psql -U postgres_app -d R57 -c "select lo_export( raster, '$path/'||name||'.jpg') from tmp_object_lo where name='$i';"

	#Changement qualité:
	#convert $path/$i.jpg -define jpeg:extent=$maxSize$ko $path/output.jpg;
	
	#Redimensionnement si img>600x800
	convert $path/$i.jpg -resize '600x800>' $path/output.jpg;

	#Insert photos modifiée 
	psql -U postgres_app -d R57 -c "update signalement_photo set image_content =(select bytea_import('$path/output.jpg')) where id_photo=$i;"
	
	#Suppression img générée + lo en base
	rm -f $path/*jpg;
	psql -U postgres_app -d R57 -c "SELECT lo_unlink(l.oid) FROM pg_largeobject_metadata l where oid=$i;"
	psql -U postgres_app -d R57 -c "delete from tmp_object_lo where name='$i';"
	
	nbModifie=$(( nbModifie+1 ));
done

#Suppression de la table
psql -U postgres_app -d R57 -c "DROP TABLE IF EXISTS tmp_object_lo;"

#Taille des photos en base 
tailleFinal=`psql -t -U postgres_app -d R57 -c "SELECT sum(octet_length(image_content)/1000000) taille_finale FROM signalement_photo;"`
echo "Nombre de photos modifiées: "$nbModifie>>$log;
echo "Poid final des photos en base:"$tailleFinal "MO">>$log;
echo "Fin du batch "`date '+%Y-%m-%d %H:%M:%S'`>>$log;
