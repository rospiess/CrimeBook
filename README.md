CrimeBook
=========

Database Project 2014


DONE
-----------
(needing code review)

- Case detail view: Show all associated convictions with links to their detailed view.

- Person detail view: Show all associated convictions / open cases (with role)  with links to their detailed view.

- Close/reopen case with convictions created/deleted.

- Basic case creation
- Person of interest creation
- Register new users.



To-Do List
-----------


- Manage NULL values in case and person creation.

When logged in:
  - Edit cases and persons.
  - Link/unlink person from case.
  



(Design-)Questions:
-----------

- What happens to a conviction when the associated case is reopened?
    -> delete convictions


- When closing a case, should we prompt the user to set date for each conviction right then, or set default values and let him edit them later?

- Should unlinking a person from a case also be possible from the persons detail view?


Ideas and Nice-to-have:
-----------

- Bounty
- Victims
- Statistics (SQL queries are done, will push the commit as soon as the frontend follows)
  - # cases per category
  - # cases per year
  - average age
  - user activity (# comments etc.)
  - Most convictions / suspect- / witnessroles
