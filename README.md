# JPA-SandBox
Basic nowledge refresher

Antipatterns:
- https://www.developerfusion.com/article/84945/flush-and-clear-or-mapping-antipatterns/
- http://presentz.org/codemotion12/performance_anti_patterns_in_hibernate_patrycja_wegrzynowicz

Performance:
- https://vladmihalcea.com/books/high-performance-java-persistence/

Read later:
- https://javabeat.net/jpa/

Questions:
- detach() vs clear()

General reminder:
- @Transactional can be used on the repository side or e.g. on unit test method side. In each case it ensures that the whole method is executed, or rolled back (rollback works also for changes enforced to be saved to DB by calling flush()). It also keeps persistence context (implementation of EntityManager interface) active, meaning e.g. lazy initialization is working correctly.
- @Transactional topic 2: Hibernate waits to the last possible moment before saving changes to DB (performance optimization, and easy possibility of rollback)
- Bi-directional transactions are designed to avoid redundancy in DB. Owning side of the relation (DB table) contains foreign key. Owned side of the relation does have to contain any foreign key, but in JPA annotation required statement: mappedBy = "passport" where passport is the attribute name from the owning entity.
- @one-to-many, owning side of the relation is many because it will contain one's id. E.g. Course has Many reviews. Owning part is Review because each review row in DB will contain course_id attribute. In JPA mapped-by will be on the owned side (which does not define xxx_id column in DB), so mapped-by is on the Course entity side

- @...-to-one => always EAGER fetching by default
- @...-to-many -> always LAZY fetching by default

Cascade types
- Entity relationships often depend on the existence of another entity — for example, the Person–Address relationship. Without the Person, the Address entity doesn't have any meaning of its own. When we delete the Person entity, our Address entity should also get deleted. Cascading is the way to achieve this. When we perform some action on the target entity, the same action will be applied to the associated entity.
- https://www.baeldung.com/jpa-cascade-types 

Read later
- DeleteOrphan
