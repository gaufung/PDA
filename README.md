**PDA**

# 1 Description
The project is about *PDA* which is related model about statistic, forecast and decision making on energy consumption.

## 1.1 Including Topics
+ Data I/O
+ Linear Programming
+ Indexes Calculating
+ ORM framework

# 2 Dependencies
This java project is organized by **maven** tool, so all dependencies are listed in `pom.xml` file.
+ mysql-connector-java: All data is stored in `mysql` database.
+ joptimizer: A open source optimization library.
+ log4j: A frequently-used log component.
+ junit: A well-know java unit test component;
+ hibernate: A ORM(Object Relationship Mapping) framewrok

# 3 Files Organization
+ org.iecas.pda.model: Contains all models which represent Co2, Energy, Production and Dmu(decision making unit).
+ org.iecas.lp: Linear programming part which is applied **Decorator Pattern**
+ org.iecas.io: Query data from database by the *year* argument.
+ LMDI: The indexes of this PDA model want to calculate.
+ SinglePeriodAAM: The attributions decomposed by lmdi in single period.
+ MultiPeriodAAM: the attributions decomposed by lmdi in multi period.
+ resource/createtable.sql: the sql command to create table.

