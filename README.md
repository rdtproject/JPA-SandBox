# JPA-SandBox
Basic nowledge refresher

## To be clarified, checked
- Spring java configuration
```java
   @Configuration
   @EnableAsync
   @EnableTransactionManagement
   @EnableCaching
   public class AppConfig {
```
## General reminder
- @Transactional can be used on the repository side or e.g. on unit test method side. In each case it ensures that the whole method is executed, or rolled back (rollback works also for changes enforced to be saved to DB by calling flush()). It also keeps persistence context (implementation of EntityManager interface) active, meaning e.g. lazy initialization is working correctly.
- @Transactional topic 2: Hibernate waits to the last possible moment before saving changes to DB (performance optimization, and easy possibility of rollback)
- Bi-directional transactions are designed to avoid redundancy in DB. Owning side of the relation (DB table) contains foreign key. Owned side of the relation does have to contain any foreign key, but in JPA annotation required statement: mappedBy = "passport" where passport is the attribute name from the owning entity.

## NamedQueries
```java   
   @NamedQueries({
   	@NamedQuery(name = NQ_GET_ALL_INVOICES, query = "select i from Invoice i")
   })
   public class Invoice extends XyzEntity {   	
   	public static final String NQ_GET_ALL_INVOICES = "Invoice.getAllInvoices";
   }
```

## NamedNativeQueries
- Abstract examples, more advanced implementation to be analyzed further
```java   
   @SqlResultMapping({
   	@SqlResultSetMapping(name = INVOICES_MAPPING, columns = @ColumnResult(name = amount, type = BigInteger.class)),
	@SqlResultSetMapping(name = INVOICES_MAPPING_2, columns = @ColumnResult(name = name, type = String.class)),
	@SqlResultSetMapping(name = INVOICES_MAPPING_3, 
	classes = {@ConstructorResult(targetClass = Invoice.class, columns = {
		@ColumnResult(name = 'id', type = BigInteger.class),
		@ColumnResult(name = 'date', type = Date.class),
		@ColumnResult(name = 'name', type = String.class)
	})})
   })
   @NamedNativeQueries({
   	@NamedNativeQuery(name = NQ_GET_ALL_INVOICES, query = "select * from Invoice", resultSetMapping = INVOICES_MAPPING)
	@NamedNativeQuery(name = NQ_GET_ALL_CLOSED_INVOICES, query = "select * from Invoice i where i.status = 'CLOSED'", resultSetMapping = INVOICES_MAPPING)
   })
   public class Invoice extends XyzEntity {   	
   	public static final String NQ_GET_ALL_INVOICES = "Invoice.getAllInvoices";
	public static final String NQ_GET_ALL_CLOSED_INVOICES = "Invoice.getAllClosedInvoices";
	public static final String INVOICES_MAPPING = "invoicesInExecutionMapping";
   }
```

## Mapping numbers
- A BigDecimal is an exact way of representing numbers. A Double has a certain precision. Working with doubles of various magnitudes (say d1=1000.0 and d2=0.001) could result in the 0.001 being dropped alltogether when summing as the difference in magnitude is so large. With BigDecimal this would not happen.
The disadvantage of BigDecimal is that it's slower, and it's a bit more difficult to program algorithms that way (due to + - * and / not being overloaded).
If you are dealing with money, or precision is a must, use BigDecimal. Otherwise Doubles tend to be good enough.
- http://jlaskowski.blogspot.com/2007/04/rozpoznanie-dziaania-adnotacji-column-i.html
```java   
@Column(name = "INVOICE_AMOUNT")
private BigDecimal amount;

@Column(precision=8, scale=2) 
private double overallRate;

@Column(precision=8, scale=2) 
private float hourlyRate;
```

## Relations
- @one-to-many, owning side of the relation is many because it will contain one's id. E.g. Course has Many reviews. Owning part is Review because each review row in DB will contain course_id attribute. In JPA mapped-by will be on the owned side (which does not define xxx_id column in DB), so mapped-by is on the Course entity side
- @...-to-one => always EAGER fetching by default
- @...-to-many -> always LAZY fetching by default

## Cascade types
- Entity relationships often depend on the existence of another entity — for example, the Person–Address relationship. Without the Person, the Address entity doesn't have any meaning of its own. When we delete the Person entity, our Address entity should also get deleted. Cascading is the way to achieve this. When we perform some action on the target entity, the same action will be applied to the associated entity.
- https://www.baeldung.com/jpa-cascade-types 

Owned side (on the many side is foreign key)
```java
@OneToMany(mappedBy = "customer", cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
private List<Invoices> invoices;
```

Owning side (this table has added column with foreign keys)
```java
@OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true, fetch = FetchType.LAZY)
@Fetch(FetchType.JOIN)
private Customer customer;
```

Owning side
```java
@ManyToOne(fetch = FetchType.LAZY, optional = true)
@Fetch(FetchType.JOIN)
@JoinColumns({@JoinColumn(name="Customer_id"), @JoinColumn(name="Customer_domain")})
private Customer customer;
```
(Owned side was a template name, or dictionary value and id not contain owning object)

```java
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(name="Invoice", joinColumns = {@JoinColumn(name="Customer_id", referenceColumnName="Id"), 
@JoinColumn(name="Cust_domain_id", referenceColumnName="Id")}
inverseJoinColumns = {@JoinColumn(name="Invoice_id", referenceColumnName="Id"), 
@JoinColumn(name="Inv_domain_id", referenceColumnName="Id")})
private Set<Customer> customers;
```
(There was no mapping on the other side. Example with complex primary key on both sides - address in linking table.)

## FetchMode
- I think that Spring Data ignores the FetchMode. I always use the @NamedEntityGraph and @EntityGraph annotations when working with Spring Data
- First of all, @Fetch(FetchMode.JOIN) and @ManyToOne(fetch = FetchType.LAZY) are antagonistic, one instructing an EAGER fetching, while the other suggesting a LAZY fetch.
- https://www.solidsyntax.be/2013/10/17/fetching-collections-hibernate/ 

```java
@Entity
@NamedEntityGraph(name = "GroupInfo.detail",
  attributeNodes = @NamedAttributeNode("members"))
public class GroupInfo {

  // default fetch mode is lazy.
  @ManyToMany
  List<GroupMember> members = new ArrayList<GroupMember>();

  …
}

@Repository
public interface GroupRepository extends CrudRepository<GroupInfo, String> {

  @EntityGraph(value = "GroupInfo.detail", type = EntityGraphType.LOAD)
  GroupInfo getByGroupName(String name);

}
```
- First of all, @Fetch(FetchMode.JOIN) and @ManyToOne(fetch = FetchType.LAZY) are antagonistic, one instructing an EAGER fetching, while the other suggesting a LAZY fetch.
- Eager fetching is rarely a good choice and for a predictable behavior, you are better off using the query-time JOIN FETCH directive:
```java
public interface PlaceRepository extends JpaRepository<Place, Long>, PlaceRepositoryCustom {

    @Query(value = "SELECT p FROM Place p LEFT JOIN FETCH p.author LEFT JOIN FETCH p.city c LEFT JOIN FETCH c.state where p.id = :id")
    Place findById(@Param("id") int id);
}

public interface CityRepository extends JpaRepository<City, Long>, CityRepositoryCustom { 
    @Query(value = "SELECT c FROM City c LEFT JOIN FETCH c.state where c.id = :id")   
    City findById(@Param("id") int id);
}
```
## Inheritence strategy
### @Inheritance(strategy = InheritanceType.SINGLE_TABLE)
- Default one, adding additional descriptor column do DB (just a string). Efficient as only one DB table, but creates mess with data integrity in DB: nullable columns as not each Object sub type in hierachy implements the same attributes. In other words no joins required (performance) but nullable columns (data integrity issue)

```java
@Entity  
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)  
@DiscriminatorColumn(name = "EmployeeType")  
public abstract class Employee {  
```
### @Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
- User dedicated table per each CONCRETE class. Query collecting all data uses UNION in DB.Disadvantage: if concrete classes have some common columns, these columns will be repeated in each DB Table. Redundancy - with 10 subclasses, 10 different tables, each with the same column if each concrete class shares the same attribute.

```java
@Entity  
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)  
public abstract class Employee {  
```

### @Inheritance(strategy = InheritanceType.JOINED)
- Creates one common DB table collecting common atributes in dedicated columns and additional tables per concrete classes. Uses join for selects, performance wise not good, but completely removes data redundancy.

```java
@Entity  
@Inheritance(strategy = InheritanceType.JOINED)  
public abstract class Employee {  
```

## @MappedSuperclass
- this is no longer a hierarchy. It is not an entity as well. It just defined common parts for another objects, but these objects are not related. So SQL query has to address each concrete class individualy, there is no longer entity called Employee.

```java
@MappedSuperclass  
public abstract class Employee {  
```

### @MappedSuperclass vs @Inheritance
MappedSuperClass must be used to inherit properties, associations, and methods.

Entity inheritance must be used when you have an entity, and several sub-entities.

You can tell if you need one or the other by answering this questions: is there some other entity in the model which could have an association with the base class?

If yes, then the base class is in fact an entity, and you should use entity inheritance. If no, then the base class is in fact a class that contains attributes and methods that are common to several unrelated entities, and you should use a mapped superclass.

## DeleteOrphan
- ????

## @EntityListeners
- ???

## Joins
### JOIN (inner join)
- Students s JOIN Courses c will skip students which do not have assigned any courses

```sql
select c, s from Course c JOIN c.students s
```

### LEFT JOIN (outer join)
- Students s LEFT JOIN Courses c will return students even if some of them do not have assigned any courses

```sql
select c, s from Course c LEFT JOIN c.students s
```

### CROSS "JOIN" (not really a join)
- Will return all students mixed with all possible courses

```sql
select c, s from Course c, Student s
```

## Transactions
### ACID
Properties in transaction management.
- Atomicity. Complete all, or nothing.
- Consistency. Keeping system in the consistent state, independently if the transaction succeeds or fails. 
- Isolation. How parallel transactions are independent from each other. Also in what scope changes made by one transaction are visible to the other transactions
- Durability. If transaction succeeds, its results should be permanent (persisted). 

### Dirty, phantom, non repeatable reads
Concepts to understand.
- Dirty read. Transaction 2 reads a value which was modified by transaction 1 before transaction 1 was committed. Transaction is reading a value which has been modified by another transaction, and this another transaction did not commit its changes yet. In case this other transaction fails, it will rollback modified value and current transaction will. 
- Non repeatable read. Transaction tries to retrieve the same data twice and it gets two different values during the same transaction. When I am reading twice the same value during transaction each time I am getting different results (as another transactions changed the value in the meantime). 
- Phantom read. Getting different results of the same query in the same transaction. In the different time I am getting different nr of rows within transaction (e.g. to Person table another transactions are inserting data, so each time I am quering for list of persons, I am getting different amount of results).

### Transaction isolation levels
Important concepts related with DB isolation.
- Read uncomitted. Any transaction reads any data wheder it is committed or not. As soon as transaction changes the data, another transactions can read this data.
- Read committed. Transaction can read data only if it is already committed by another transaction. In other words as soon as transaction A is doing something with specific values, all other transactions cannot read these values (so each value is locked until transaction A commits).
- Repeatable read. Locks not only values modified during transaction, but also any data which was read, e.g. the whole DB row. So if transaction A read row 121 from DB, this whole row will not be available to any other transaction. As soon as transaction A commits, a lock from this row will be removed and it will be enabled to other transactions.
- Serializable. Having query e.g. Select * from Person p where p.age between 5 and 55. A lock is created for all rows matching this constraint. Whenever another transaction is trying to insert / modify data which satisfied this constraint it won't be able. So called table lock.

n/a | Dirty read | Non repeatable read | Phantom read | Real world
-- | ---------- | ------------------- | ------------ | -----------
**Read uncommitted** | Possible | Possible | Possible | 
**Read committed** | Solved | Possible | Possible | This one is used in most of the projects
**Repeatable read** | Solved | Solved | Possible | 
**Serializable** | Solved | Solved | Solved | Very slow performance. Having e.g. thousand transactions, 999 would have to wait until this one will be committed.

### JPA transaction vs Spring transaction
Transaction engine | Transaction type | Specifics
-- | ---------- | --------
javax.transaction.Transactional | JPA | Can handle singe DB communication. Can be used in apps using Spring.
org.springframework.transaction.annotation.Transactional | Spring | Can handle in single transaction different DBs, MQs. Allows to set transaction isolation level. Cannot be used in apps which do not use Spring framework.

https://www.baeldung.com/spring-vs-jta-transactional, and so on.

Diverse operations as below can be handled by single Spring transaction. It cannot be handled by JPA transaction.
```java
// database 1
  // update 1
  // update 2
// database 2
  // update 3
// mq call
```
Global transaction isolation level can be set up for Hibernate, e.g.:
```java
spring.jpa.properties.hibernate.connection.isolation=2
```
### Read more
- https://www.marcobehler.com/guides/spring-transaction-management-transactional-in-depth

## Spring Transactional
In latest project only Spring Transactional was used. JPA Transactional wasn't used at all.
- to be checked readonly
```java
@Transactional(readonly = true)
public void distributeTransientAttributes() {
	// business logic
}
```
- to be checked rollbacks
```java
@Transactional(rollbackFor = EntityNotFoundException.class, AnotherException.class, IncorrectParameters.class)
public void distributeTransientAttributes() {
	// business logic
}
```
```java
@Transactional(noRollbackFor = EntityNotFoundException.class)
public void distributeTransientAttributes() {
	// business logic
}
```

## Caching
UI=Web -> Service -> Data -> Database
JPA caching is ensured in Data layer.  
PersistenceContext -> FirstLevelCache -> SecondLevelCache -> Database

### First Level Cache vs Second Level Cache
<table>
    <thead>
        <tr>
            <th>Transaction1</th>
            <th>Transaction2</th>
            <th>Transaction3</th>
            <th>Transaction4</th>
            <th>All these Transactions are going on in parallel</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PersistenceContext1</td>
            <td>PersistenceContext2</td>
            <td>PersistenceContext3</td>
            <td>PersistenceContext4</td>
            <td>Each transaction has PC on its own - all entities modified within this transaction are tracked.</td>          
        </tr>
        <tr>
            <td>First Level Cache</td>
            <td>First Level Cache</td>
            <td>First Level Cache</td>
            <td>First Level Cache</td>
            <td>E.g. Transaction 1 is reading list of Courses from DB 3 times. Only the first time it will retrieve data from DB. Each next time it will return the result from L1 cache. L1 cache is within the boundary of the single transaction.</td>          
        </tr>      
        <tr>
            <td colspan=4 align=center>Second Level Cache</td>
            <td>Comes across multiple transactions. E.g. many users are using app. All are getting a static list of countries. List of countries is always the same, so can be safely stored in L2 cache. First transaction which will reach DB, will query data, and since now any other transaction will just retrieve this information from Cache.</td>
        </tr>
        <tr>
            <td colspan=4 align=center>Database</td>
            <td>Access to DB is always slow.</td>
        </tr>
    </tbody>
</table>

### First Level Cache
- This code will execute query to DB only once because everything is wrapped up into single Transaction (Transactional on the method level) and we are quering the same data.
- Remember that if L1 Cache becomes to big (Entity Manager keeps to many cached operations) it is starting to become slow.
```java
@Transactional
public void findByIdFirstLevelCache {  
  Course course = repository.findById(1001L);
  Course course1 = repository.findById(1001L);
  Course course2 = repository.findById(1001L);
}
```

After removing Transactional from method level, each call to repository is done withing separate transaction (provided by repository). We do not have anymore single PersistenceContext.
```java
public void findByIdFirstLevelCache {  
  Course course = repository.findById(1001L);
  Course course1 = repository.findById(1001L);
  Course course2 = repository.findById(1001L);
}
```
- The most efficient L1 cache can be ensured by putting Transactional on the service method level, as on the example above.
- L1 is active by default, no configuration required.

### Second Level Cache
- Requires configuration
- Hibernate does not know which data is not going to change, and will be common to multiple transactions (e.g. such data can be list of domains in WPG, list of countries, currencies, and another dictionary-like values)
- Implementation of L2 cache can be done using EhCache (it is a caching framework)
- Example how to enable L2 cache: https://www.baeldung.com/hibernate-second-level-cache

### Distributed Cache
- L2 Cache is used on single JVM, single application instance but many transactions
- Distrbited cache, e.g. Hazelcast, more complex, used to cache withing multiple application instances

#### Adding required dependencies
```java
<dependency>
	<groupId>org.hibernate</groupId>
	<artifactId>hibernate-ehcache</artifactId>
</dependency>
```
#### Setting up configuration in application.properties
```properties
#Enable EhCache
spring.jpa.properties.hibernate.cache.use_second_level_cache=true

#Specify caching framework
spring.jpa.properties.hibernate.cache.region.factory_class=org.hibernate.cache.ehcache.EhCacheRegionFactory

#Only cache what I tell to cache
spring.jpa.properties.javax.persistence.sharedCache.mode=ENABLE_SELECTIVE
```
- Value ENABLE_SELECTIVE comes from:
```java
package javax.persistence;

public enum SharedCacheMode {
    ALL,
    NONE,
    ENABLE_SELECTIVE,
    DISABLE_SELECTIVE,
    UNSPECIFIED;
}
```

#### To enable caching only what I tell to cache 
- Only data which is safe to cache between transactions / does not change often.
```java
@Cacheable //javax.persistence.Cacheable
public class Course {
```
#### How to read L2 cache logs
L2C stands for Lever 2 Cache
- L2C puts: Entity with selected id was put in cache. Next time any transaction will try to read it, query to db will no longer be needed.
- L2C hits: When I am looking at the cache for entity with selected nr, and I get the data (query to DB was not needed).
- L2C misses: Entity with selected id was not cached. Query to DB was required.
- Test with e.g. http://localhost:8080/courses/10001 having enabled Spring Data Rest
```properties
2020-11-13 21:40:38.120  INFO 20104 --- [           main] i.StatisticalLoggingSessionEventListener : Session Metrics {
    29800 nanoseconds spent acquiring 1 JDBC connections;
    0 nanoseconds spent releasing 0 JDBC connections;
    22100 nanoseconds spent preparing 1 JDBC statements;
    79800 nanoseconds spent executing 1 JDBC statements;
    0 nanoseconds spent executing 0 JDBC batches;
    0 nanoseconds spent performing 0 L2C puts;
    32300 nanoseconds spent performing 1 L2C hits;
    0 nanoseconds spent performing 0 L2C misses;
    0 nanoseconds spent executing 0 flushes (flushing a total of 0 entities and 0 collections);
    0 nanoseconds spent executing 0 partial-flushes (flushing a total of 0 entities and 0 collections)
}
```
- 1 L2C hits because Course with id 10001 was already cached. Each new transaction will just get this course from Cache. 
- Reviews are not cached (as can change more often), do each time respective DB query will be executed.
```json
{
  "name" : "JPA in 50 steps",
  "reviews" : [ {
    "rating" : "5",
    "description" : "Great course",
    "_links" : {
      "course" : {
        "href" : "http://localhost:8080/courses/10001"
      }
    }
  }, {
    "rating" : "4",
    "description" : "Average",
    "_links" : {
      "course" : {
        "href" : "http://localhost:8080/courses/10001"
      }
    }
  } ],
  "lastUpdateDate" : "2020-11-13T00:00:00",
  "creationDate" : "2020-11-13T00:00:00",
  "_links" : {
    "self" : {
      "href" : "http://localhost:8080/courses/10001"
    },
    "course" : {
      "href" : "http://localhost:8080/courses/10001"
    }
  }
}
```
## Soft deletes
This is Hibernate feature, not JPA feature. Can be used for cases, when all removed data should not be physically removed from DB, but rather marked as "removed".
- Add isDeleted attribute (respective default column in DB will be is_deleted). Setters, getters not required.
- Add @SQLDelete annotation and specify JPQL which will be always appended to any "where" query.
- Add @@Where annotation and specify JPQL to be executed for any query.
- Does not work for native queries! @Where confition will not be automatically added.
- Native queries do not set up automatically flag is_deleted, so @SQLDelete is ignored.
```java
@SQLDelete(sql = "update Course set is_deleted=true where id=?")
@Where(clause = "is_deleted = false")
public class Course {
	// .....
	private boolean isDeleted;
	// .....
}
```
Solution to the concerns with native queries can be to use lifecycle methods
```java
	private boolean isDeleted;
	
	private void preRemove() {
		this.isDeleted = true;
	}
```

## Embedded, Embeddable
- Where entity does not have to be in any relation, but should become a part of main Entity DB.
- Embedded entity can be still used in another places

Class which can be embedded
```java
@Embeddable
public class Address {
}
```
Embedded attribute in Student entity.
```java
@Embedded
private Address address;
```
Table student now contains more columns.
```java
create table student (
       id bigint not null,
        city varchar(255),
        line1 varchar(255),
        line2 varchar(255),
        name varchar(255) not null,
        passport_id bigint,
        primary key (id)
)
```
## Using Enums
Enums which should be stored in DB has to be annotated as below.
Default: Ordinal, can be updated to String.
```java
@Enumerated(EnumType.STRING)
private ReviewRating reviewRating;
```
```java
public enum ReviewRating {
    ONE, TWO, THREE, FOUR, FIVE;
}
```
## ToString
Watch out not to put too many information there - can cause perfomance issues, e.g. lazy initialized properties would be loaded due to some log.info() pronting the whole object data.

## Antipatterns
- https://www.developerfusion.com/article/84945/flush-and-clear-or-mapping-antipatterns/
- http://presentz.org/codemotion12/performance_anti_patterns_in_hibernate_patrycja_wegrzynowicz

## Performance tuning
https://vladmihalcea.com/books/high-performance-java-persistence/
### Measure and fine tune
- Donald Knuth: We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil.
- Before any performance tuning it is absolutely mandatory to enable and monitor stats in at least one environment.
```properties
#Turn statistics on
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=debug
```
### Add indexes
- To add indexes it is good to check the query execution plan
- Index is automatically created for the primary key, id.
- If e.g. Course entity is often searched by name, than it makes sense to add index on name, etc.

### Appropriate caching
- First level cache enabled automatically, working on the single transaction level. If too many entoties are stored in L1, it can cause performance issue as well.
- Second Level caching enables different transactions on the same instance of an application to share the common data. Countries, States, etc. Must be enabled manually. E.g. EhCache.
- Distributed cache used to cache data across multiple instances of application. E.g. Hazelcast.

### N+1 problem
- https://www.sipios.com/blog-tech/eliminate-hibernate-n-plus-1-queries 
- Occurs for cases as below. Many to many with mode: Lazy. For each loop iteration additional select to DB is executed:
```java
    @Test
    @Transactional
    public void getAllCourses() {
        List<Course> courses = em.createNamedQuery(EntityConstans.GET_ALL_COURSES, Course.class)
                .getResultList();

        for (Course course : courses) {
            logger.info("Course => {}, Students => {}", course, course.getStudents());
        }
    }
```
- select "+1"
```sql
    select
        course0_.id as id1_0_,
        course0_.created as created2_0_,
        course0_.is_deleted as is_delet3_0_,
        course0_.updated as updated4_0_,
        course0_.name as name5_0_ 
    from
        course course0_ 
    where
        (
            course0_.is_deleted = 0
        )
```
- select N, below is repeated for each loop iteration
```sql
    select
        students0_.course_id as course_i2_7_0_,
        students0_.student_id as student_1_7_0_,
        student1_.id as id1_6_1_,
        student1_.city as city2_6_1_,
        student1_.line1 as line3_6_1_,
        student1_.line2 as line4_6_1_,
        student1_.name as name5_6_1_,
        student1_.passport_id as passport6_6_1_ 
    from
        student_course students0_ 
    inner join
        student student1_ 
            on students0_.student_id=student1_.id 
    where
        students0_.course_id=?
```
#### Solution 1, using Join Fetch
- JPQL query, use the keyword JOIN FETCH:
```java
@NamedQuery(name = GET_ALL_COURSES_FETCH, query = "select c from Course c JOIN FETCH c.students")
```

#### Solution 2, using Entity Graph
- JPA query, use an entity graph
- http://www.radcortez.com/jpa-entity-graphs/
```java
        EntityGraph<Course> entityGraph = em.createEntityGraph(Course.class);
        entityGraph.addSubgraph("students");

        List<Course> courses = em.createNamedQuery(EntityConstans.GET_ALL_COURSES, Course.class)
                .setHint("javax.persistence.loadgraph", entityGraph)
                .getResultList();
```
#### Solution 2, can be improved like this
```java   
   @NamedQueries({
   	@NamedQuery(name = NQ_GET_ALL_INVOICES, query = "select i from Invoice i")
   })
   @NamedEntityGraph(
   	name = EG_INVOICE_WITH_DETAILS,
	attributeNodes = {
		@NamedAttributeNode(creator)
		@NamedAttributeNode(contact)
		@NamedAttributeNode(amount)
		@NamedAttributeNode(processor)
	}
   )
   public class Invoice extends XyzEntity {   	
   	public static final String NQ_GET_ALL_INVOICES = "Invoice.getAllInvoices";
	public static final String EG_INVOICE_WITH_DETAILS = "InvoiceWithCreator";
   }
```

```java
public interface GenericDao<T> {
    /** You specify FETCH as your strategy by importing javax.persistence.fetchgraph in the file containing the entity.
        In this case, all attributes specified in your entity graph will be treated as FetchType.EAGER, and all attributes
	not specified will be treated as FetchType.LAZY*/
	String FETCH_GRAPH_HINT = "javax.persistence.fetchgraph";
	
    /** On the other hand, if you specify LOAD as your strategy by importing javax.persistence.loadgraph then all attributes
        specified in the entity graph are also FetchType.EAGER but attributes not specified use their specified type or default 
	if the entity specified nothing. */		
	String LOAD_GRAPH_HINT = "javax.persistence.loadgraph";
}
```

```java
public interface CourseDao extends GenericDao<Course> {	
}
```
```java
public class CourseDaoImp implements CourseDao {	
	TypedQuery<Course> typedQuery = entityManager.createNamedQuery(EntityConstans.GET_ALL_COURSES, Course.class);
	typedQuery.setHint(LOAD_GRAPH_HINT, entityManager.getEntityGraph(EG_INVOICE_WITH_DETAILS));
	return typedQuery.getResultList();
}
```

- Or as mentioned in attached tutorial
```java
@EntityGraph(attributePaths = {"author"})
List<Message> getAllBy();
```

## SpringDataRest
Add dependency:
```xml
<dependency>
	<groupId>org.springframework.boot</groupId>
	<artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>
```
Add annotation @RepositoryRestResource and initial path for RESTful services:
```java
@RepositoryRestResource(path = "courses")
public interface CourseSpringDataRepository extends JpaRepository<Course, Long> {
```
In Course entity add annotation @JsonIgnore to avoid infinity loops (Course has Students, but each student can have course, which can have student, which can have...)
```java
@ManyToMany(mappedBy = "courses")
@JsonIgnore
private List<Student> students = new ArrayList<>();
```

## Enable H2 Console
http://localhost:8080/h2-console  
In application.properties:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.data.jpa.repositories.bootstrap-mode=default

#Enabling H2 console
spring.h2.console.enabled=true
```

## Enable logging
Enable SQL logging including query parameters
```properties
#Turn statistics on
spring.jpa.properties.hibernate.generate_statistics=true
logging.level.org.hibernate.stat=debug

#Show all queries
spring.jpa.show-sql=true

#Show parameters
logging.level.org.hibernate.type=trace

#Format queries - do not enable on Production!
spring.jpa.properties.hibernate.format_sql=true
```

## Evolutionary Database Design
- Martin Fowler, Evolutionary database design: https://martinfowler.com/articles/evodb.html 
