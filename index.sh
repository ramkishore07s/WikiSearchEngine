time java -DentityExpansionLimit=2147480000 -DtotalEntitySizeLimit=2147480000 -Djdk.xml.totalEntitySizeLimit=2147480000 -jar indexer.jar $1 6000 final_postings

python concatenate_names.py
python second.py
