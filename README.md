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
- @Transactional can be used on the repository side or e.g. on unit test method side. In each case it ensures that the whole method is executed, or rolled back. It also keeps persistence context (implementation of EntityManager interface) active, meaning e.g. lazy initialization is working correctly.
- Bi-directional transactions are designed to avoid redundancy in DB. Owning side of the relation (DB table) contains foreign key. Owned side of the relation does have to contain any foreign key, but in JPA annotation required statement: mappedBy = "passport" where passport is the attribute name from the owning entity.
