### How Representative is a SPARQL Benchmark? An Analysis of RDF Triplestore Benchmarks
We provide a fine-grained comparative analysis of existing triplestore benchmarks. In particular, we have analyzed the data and queries, provided with the existing triplestore benchmarks in addition to several real-world datasets. Further, we have measured the correlation between the query execution time and various SPARQL query features and ranked those features based on their significance levels. Our experiments have revealed  several interesting insights about the design of such benchmarks. We can hope such fine-grained evaluation will be helpful for SPARQL benchmark designers to design diverse benchmarks in the future. 

### Analysis
Our analysis is based on the following benchmark design features: 
* Dataset structuredness
* Dataset relationship specialty
* Overall queries diversity score based on important SPARQL query features, i.e., number of triple patterns, number of projection variables, result set sizes, query execution time, number of BGPs, number of join vertices, mean join vertex degree, mean triple pattern selectivities, BGP-restricted and join-restricted triple pattern selectivities, and join vertex types. 
* Percentages-wise distribution of the use of important SPARQL clauses (e.g., LIMIT, OPTIONAL, ORDER BY, DISTINCT,
UNION, FILTER, REGEX) in benchmark queries 
* SPearsman's correlation of the query runtimes and important SPARQL query features

The first two features are related to benchmark datasets and later three are related to benchmark queries. Please refer to the manuscript for the details of above design features. 

### Reproducing Results
Please follow the following steps to reproduce the complete results presented in the paper. 
 Markup : 1. A numbered list
              1. A nested numbered list
              2. Which is numbered
          2. Which is numbered
folder [cli](https://github.com/AKSW/triplestore-benchmarks/tree/master/cli) which contains a runable jar benchmark-util.jar and three queries file required as input for generating results (explained below). Note the jar requires the LSQ dataset endpoint URL to be provided as input. We have provided the Virtuoso 7.2 endpoints both for SWDF and DBpedia datasets which can be downloaded from [here](http://hobbitdata.informatik.uni-leipzig.de/sqcframework-lsq-endpoints/). The Windows virtuoso endpoint can be started from bin/start.bt while linux can be started from bin/start_virtuoso.sh.  
From the folder run the following commands: 
```html
### DBSCAN+Kmeans++ Format ### 
 java -jar sqcframwork.jar -m <method> -n <noQueries> -i <maxNoIterations> -t <noTrialRun> -e <endpointUrl> -q <queryPersonalized> -r <radius> -p <minPts> -o <outputFile>
An example format: 
java -jar sqcframework.jar   -m db+km++   -n 10   -i 10   -t 10   -e http://localhost:8890/sparql   -q personalized-query.txt   -r 1   -p 1   -o db+km++-10supqueries-benchmark.ttl

### Kmeans++ Format ### 
 java -jar sqcframwork.jar -m <method> -n <noQueries> -i <maxNoIterations> -t <noTrialRun> -e <endpointUrl> -q <queryPersonalized> -o <outputFile>
An example format: 
java -jar sqcframework.jar   -m km++   -n 10   -i 10   -t 10   -e http://localhost:8890/sparql   -q personalized-query.txt   -o km++-10supqueries-benchmark.ttl


### FEASIBLE Format ### 
 java -jar sqcframwork.jar -m <method> -n <noQueries> -e <endpointUrl> -q <queryPersonalized> -o <outputFile>
An example format: 
java -jar sqcframework.jar   -m feasible   -n 10  -e http://localhost:8890/sparql   -q personalized-query.txt   -o feasible-10supqueries-benchmark.ttl


### Agglomerative Format ### 
 java -jar sqcframwork.jar -m <method> -n <noQueries> -e <endpointUrl> -q <queryPersonalized> -o <outputFile>
An example format: 
java -jar sqcframework.jar   -m agglomerative   -n 10  -e http://localhost:8890/sparql   -q personalized-query.txt   -o agglomerative-10supqueries-benchmark.ttl


### FEASIBLE-Exemplars Format ### 
 java -jar sqcframwork.jar -m <method> -n <noQueries> -e <endpointUrl> -q <queryPersonalized> -o <outputFile>
An example format: 
java -jar sqcframework.jar   -m feasible-exmp   -n 10  -e http://localhost:8890/sparql   -q personalized-query.txt   -o feasible-exmp-10supqueries-benchmark.ttl


### Random Selection Format ### 
 java -jar sqcframwork.jar -m <method> -n <noQueries> -e <endpointUrl> -q <queryPersonalized> -o <outputFile>
An example format: 
java -jar sqcframework.jar   -m random   -n 10  -e http://localhost:8890/sparql   -q personalized-query.txt   -o random-10supqueries-benchmark.ttl

Where

noQueries = Number of super-queries in the benchmark
maxNoIterations = Maximum number of iterations for the KMeans++ clustering algorithm. In our evaluation we used maxNoIterations = 10. 
noTrialRun = Number of trial run for the KMeans++ clustering algorithm. In our evaluation we used noTrialRun = 10.
endpointURL = The LSQ endpoint URL containing containment relationships as well
queryPersonalized = The personalized query for costum benchmark generation
radius = Radius for the queries to be considered as outliers. In our evaluation we used radius = 1
minPts = Minimum points or queries in a cluster. In our evaluation we used min. points = 1
outputFile = The output TTL file where the resulting benchmark will be printed

```
### Generating Benchmarks from Source 
Download the source code from [here](https://github.com/AKSW/sqcframework/blob/master/SQCFramework-src.7z). Unzip the folder which contains 4 -- Agglomerative, commons-math3, FEASIBLE, SQCFramework -- java projects. SQCFramework is the main project from where benchmarks can be generated. Note this project requires the other 3 project to be included in the build path. 
```
//Generate KMeans++ benchmarks from 
package org.aksw.simba.sqcbench.centroid
public class KmeansPlusPlus 

//Generate DBSCAN+KMeans++ benchmarks from 
package org.aksw.simba.sqcbench.hybrid
public class DbscanAndKMeansPluPlus 

//Generate FEASIBLE benchmarks from 
package org.aksw.simba.sqcframework.feasible
public class FEASIBLEClustering 

//Generate FEASIBLE-Exemplars benchmarks from 
package org.aksw.simba.sqcframework.feasible
public class FeasibleExemplars

//Generate Random selection benchmarks from 
package org.aksw.simb.sqcbench.random
public class RandomSelection

//You can also generate Agglomerative benchmarks from 
package org.aksw.simba.sqcbench.hierarchical
public class Agglomerative
However, Agglomerative clustering does not allow to generate fix number of clusters
```
### SPARQL Containment Solvers
We used four -- JSAC, TreeSolver, SPARQL-Algebra, AFMU -- SPARQL query containment solvers in our evaluation. Running these solvers can be found at [here](https://github.com/AKSW/jena-sparql-api/tree/master/benchmarking/sparqlqc-jena3). 
### LSQ Datasets
The LSQ datasets can be downloaded from [here](http://hobbitdata.informatik.uni-leipzig.de/lsq-dumps/)
### Complette Evaluation Results
Our complete evaluation results can be found [here](https://github.com/AKSW/sqcframework/blob/master/SQCFramework-Evaluation-Results.xlsx)
### Authors
  * [Muhammad Saleem](https://sites.google.com/site/saleemsweb/) (AKSW, University of Leipzig) 
  * [Claus Stadler](http://aksw.org/ClausStadler.html) (AKSW, University of Leipzig)
  * [Qaiser Mehmood](https://www.insight-centre.org/users/qaiser-mehmood) (INSIGHT, University of Galway) 
  * [Jens Lehmann](http://jens-lehmann.org/) (University of Bonn)
  * [Axel-Cyrille Ngonga Ngomo](http://aksw.org/AxelNgonga.html) (AKSW, University of Leipzig)
