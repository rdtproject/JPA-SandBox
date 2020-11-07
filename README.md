# JPA-SandBox
Basic nowledge refresher

## Antipatterns
- https://www.developerfusion.com/article/84945/flush-and-clear-or-mapping-antipatterns/
- http://presentz.org/codemotion12/performance_anti_patterns_in_hibernate_patrycja_wegrzynowicz

## Performance
- https://vladmihalcea.com/books/high-performance-java-persistence/

## Read later
- https://javabeat.net/jpa/

## Questions
- detach() vs clear()

## General reminder
- @Transactional can be used on the repository side or e.g. on unit test method side. In each case it ensures that the whole method is executed, or rolled back (rollback works also for changes enforced to be saved to DB by calling flush()). It also keeps persistence context (implementation of EntityManager interface) active, meaning e.g. lazy initialization is working correctly.
- @Transactional topic 2: Hibernate waits to the last possible moment before saving changes to DB (performance optimization, and easy possibility of rollback)
- Bi-directional transactions are designed to avoid redundancy in DB. Owning side of the relation (DB table) contains foreign key. Owned side of the relation does have to contain any foreign key, but in JPA annotation required statement: mappedBy = "passport" where passport is the attribute name from the owning entity.
- @one-to-many, owning side of the relation is many because it will contain one's id. E.g. Course has Many reviews. Owning part is Review because each review row in DB will contain course_id attribute. In JPA mapped-by will be on the owned side (which does not define xxx_id column in DB), so mapped-by is on the Course entity side

- @...-to-one => always EAGER fetching by default
- @...-to-many -> always LAZY fetching by default

## Cascade types
- Entity relationships often depend on the existence of another entity — for example, the Person–Address relationship. Without the Person, the Address entity doesn't have any meaning of its own. When we delete the Person entity, our Address entity should also get deleted. Cascading is the way to achieve this. When we perform some action on the target entity, the same action will be applied to the associated entity.
- https://www.baeldung.com/jpa-cascade-types 

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
- Read uncomitted.
- Read committed.
- Repeatable read.
- Serializable
| df | Dirty read | Non repeatable read | Phantom read |
|-- |-- |-- |-- |
|Read uncommitted | Possible | Possible | Possible |
