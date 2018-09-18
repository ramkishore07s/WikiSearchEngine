time java -DentityExpansionLimit=2147480000 -DtotalEntitySizeLimit=2147480000 -Djdk.xml.totalEntitySizeLimit=2147480000 -jar Java/indexer.jar $1 6000 final_postings

python Python/concatenate_names.py
python Python/second.py

mv tert.py Python/
mv tert_names.py Python/
