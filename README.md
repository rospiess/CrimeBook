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


- Add distinction between association to a case and conviction (Person detail view)
- Manage NULL values in case and person creation.

When logged in:
  - Edit cases and persons.
  - Link/unlink person from case.
  



(Design-)Questions:
-----------

- What happens to a conviction when the associated case is reopened?
    -> delete convictions

- Set datatype of streetno and zipcode in Address to String, so that we can display "unknown" or "???". 
The alternative would be to write some complicated unknown handling code at multiple places ;)

- When closing a case, should we prompt the user to set date for each conviction right then, or set default values and let him edit them later?

- Should unlinking a person from a case also be possible from the persons detail view?


Ideas and Nice-to-have:
-----------

- Bounty

