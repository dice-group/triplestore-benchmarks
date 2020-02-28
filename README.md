### How Representative is a SPARQL Benchmark? An Analysis of RDF Triplestore Benchmarks (TheWebConf2019 paper)
We provide a fine-grained comparative analysis of existing triplestore benchmarks. In particular, we have analyzed the data and queries, provided with the existing triplestore benchmarks in addition to several real-world datasets. Further, we have measured the correlation between the query execution time and various SPARQL query features and ranked those features based on their significance levels. Our experiments have revealed  several interesting insights about the design of such benchmarks. We can hope such fine-grained evaluation will be helpful for SPARQL benchmark designers to design diverse benchmarks in the future.
### Persistent URI, Licence 
All of the data and results presented in our evaluation are available online from
https://github.com/dice-group/triplestore-benchmarks under [GNU General Public License v3.0](https://github.com/dice-group/triplestore-benchmarks/blob/master/LICENSE).

### Benchmark Datasets and Queries
We provide the datasets and queries of the benchmarmks and real-world datasets used in our evaluation. The datasets are also provided as portable virtuoso triplestores which can be started from bin/start_virtuoso.sh.

| *Benchmark/Dataset*   | *RDF Dump* | *Virtuoso Store* | *Queries* |
|-----------------------|------------|---------------------|-----------|
|[Bowlogna](https://exascale.info/assets/pdf/BowlognaBenchSIMPDA2011.pdf)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/datasets-dumps/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[TrainBench](http://docs.inf.mit.bme.hu/trainbenchmark/)|[Download](https://www.dropbox.com/s/n7s02dzf0dyf4by/trainbenchmark-models-1-1024.zip?dl=0)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[BSBM](https://pdfs.semanticscholar.org/0efc/d1d38ad020da7c01613b7818eb123cb34121.pdf)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/datasets-dumps/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[SP2Bench](https://arxiv.org/pdf/0806.4627.pdf)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/datasets-dumps/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[WatDiv](https://link.springer.com/chapter/10.1007/978-3-319-11964-9_13)|[Download](http://dsg.uwaterloo.ca/watdiv/watdiv.100M.tar.bz2)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[LDBC-SNB](https://ldbc.github.io/ldbc_snb_docs/wiki)|[Download](https://www.dropbox.com/s/uyocuxmx85dce4m/social_network_ttl_sf1.zip?dl=0)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[FEASIBLE](https://svn.aksw.org/papers/2015/ISWC_FEASIBLE/public.pdf)|[Download](http://downloads.dbpedia.org/3.5.1/en/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[FishMark](http://ceur-ws.org/Vol-943/SSWS_HPCSW2012_paper1.pdf)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/datasets-dumps/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[DBPSB](https://link.springer.com/chapter/10.1007/978-3-642-25073-6_29)|[Download](http://downloads.dbpedia.org/3.5.1/en/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[BioBench](https://jbiomedsem.biomedcentral.com/track/pdf/10.1186/2041-1480-5-32)|ftp://ftp.dbcls.jp/togordf/bmtoyama/|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/queries/) |
|[DBpedia3.5.1](http://wiki.dbpedia.org/)|[Download](http://downloads.dbpedia.org/3.5.1/en/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-lsq-results.virtuoso.tar.gz)|
|[SWDF](https://old.datahub.io/dataset/semantic-web-dog-food)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/datasets-dumps/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-lsq-results.virtuoso.tar.gz)|
|[NCBIGene](http://download.openbiocloud.org/release/3/ncbigene/ncbigene.html)|[Download](http://download.bio2rdf.org/#/release/3/ncbigene/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-lsq-results.virtuoso.tar.gz)|
|[SIDER](http://download.openbiocloud.org/release/3/sider/sider.html)|[Download](http://download.bio2rdf.org/#/release/3/sider/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-lsq-results.virtuoso.tar.gz)|
|[DrugBank](http://download.openbiocloud.org/release/3/drugbank/drugbank.html)|[Download](http://download.bio2rdf.org/#/release/3/drugbank/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/)|[Download](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-lsq-results.virtuoso.tar.gz)|


### Analysis
Our analysis is based on the following benchmark design features:
* Dataset structuredness (**dataset related**)
* Dataset relationship specialty (**dataset related**)
* Overall queries diversity score based on important SPARQL query features, i.e., number of triple patterns, number of projection variables, result set sizes, query execution time, number of BGPs, number of join vertices, mean join vertex degree, mean triple pattern selectivities, BGP-restricted and join-restricted triple pattern selectivities, and join vertex types. (**queries related**)
* Percentages-wise distribution of the use of important SPARQL clauses (e.g., LIMIT, OPTIONAL, ORDER BY, DISTINCT,
UNION, FILTER, REGEX) in benchmark queries  (**queries related**)
* SPearsman's correlation of the query runtimes and important SPARQL query features (**queries related**)

The first two features are related to benchmark datasets and later three are related to benchmark queries. Please refer to the manuscript for the details of above design features.

### Reproducing Results
Please follow the following steps to reproduce the complete results presented in the paper.
 1. Download the folder [CLI](https://github.com/AKSW/triplestore-benchmarks/tree/master/cli) which contains a runable jar **benchmark-util.jar**.  
 2. To calculate the structuredness or relationship specialty of an RDF datasets, we need to first load the dataset into a triple store and provide the endpoint url as input to the jar file. The linux based virtuoso SPARQL endpoints of the datasets of all the triplestore benchmarks and real-world datasets used in our evaluation can be downloaded from [here](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-datasets-virtuoso/). Note the virtuoso can be started from bin/start_virtuoso.sh script. The utility work for any SPARQL endpoint.
 3. Download the [Virtuoso](https://hobbitdata.informatik.uni-leipzig.de/benchmarks-data/benchmarks-lsq-results.virtuoso.tar.gz) which contains the LSQ datasets of all the selected 10 triplestores benchmarks and 5 real-world datasets. Please refer to [LSQ homepage](https://github.com/aksw/lsq) for generating an LSQ dataset of the queries of a new RDF benchmark. This step is only required to generate queries related results.

 ```html
###Command line arguments ###
java -jar benchmark-util.jar  -m <measure> -e <endpoint> -g <graph> -q <queriesFile>

where

measure = structuredness or specialty or diversity or percentage or correlation
endpoint = endpoint url
graph = graph name (optional)
queriesFile = queries file (only required to produce queries related statistics, i.e., diversity, percentages, and correlation).
 please note the queries files are already provided with the cli folder downloaded in step 1.

An example formats:
java -jar benchmark-util.jar -m structuredness -e http://localhost:8890/sparql
java -jar benchmark-util.jar -m specialty -e http://localhost:8890/sparql -g http://benchmark-eval.aksw.org/feasible

java -jar benchmark-util.jar -m diversity -e http://localhost:8890/sparql -g http://benchmark-eval.aksw.org/feasible -q queries-diversity.txt
java -jar benchmark-util.jar -m percentage -e http://localhost:8890/sparql -q queries-percent.txt -g http://benchmark-eval.aksw.org/biobench
java -jar benchmark-util.jar -m correlation -e http://localhost:8890/sparql -q queries-correlation.txt -g http://benchmark-eval.aksw.org/dbpsb

You can run SPARQL SELECT DISTINCT ?g WHERE { GRAPH ?g {?s ?p ?o }} on the virtuoso downloded in step 2 to get the graph names of all the selected benchmarks and real-world datasets. Note you can add more queries into the input files in -q argument to get results for other features.
```

### Complette Evaluation Results
Our complete evaluation results can be found [here](https://github.com/AKSW/triplestore-benchmarks/raw/master/complete-evaluation-results.xlsx)
### Authors
  * [Muhammad Saleem](https://sites.google.com/site/saleemsweb/) (AKSW, University of Leipzig)
  * [Gábor	Szárnyas](https://inf.mit.bme.hu/en/members/szarnyasg/) (MTA-BME Lendület Cyber-Physical Systems Research Group, Budapest University of Technology and Economics)
  * [Felix Conrads](http://aksw.org/FelixConrads.html) (AKSW, University of Leipzig)
  * [Syed Ahmad Chan	Bukhari](http://ahmadchan.com) (Department of Pathology, Yale University School of Medicine)
  * [Qaiser Mehmood](https://www.insight-centre.org/users/qaiser-mehmood) (INSIGHT, University of Galway)
  * [Axel-Cyrille Ngonga Ngomo](http://aksw.org/AxelNgonga.html) (AKSW, University of Leipzig)
