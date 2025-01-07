# javalottery
Programming 1 course project

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

LOTTERY MACHINE

This is a Java lottery game, set up for 1-10 players and 1-100 tickets per round.

It includes:
- main menu
- prize table with different win combinations
- option to generate randomized tickets for a specific player
- option to input tickets with specific numbers for a specific player
- option to show all players and tickets currently in the round

Playing the round does the following:
- generates a randomized winning ticket
- compares all the tickets in the round against the winning ticket
- counts the correct numbers and bonus numbers for each ticket
- allocates the winnings to a specific player
- prints out all these results at the end
- allows resetting the game without quitting 

Edge cases covered:
- all inputs are checked for type and range
	* the game tracks available player slots and available tickets, and checks the user inputs
- it's not possible to create more than 100 tickets
- it's not possible to create more than 10 players
- it's always possible to create up to 100 tickets
	* if there are available tickets left but no player spots, allocating more tickets to existing players is possible
- it's not possible to play a round with no tickets

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

Future considerations (not implemented currently):
- it's not possible to quit to menu in the middle of ticket creation
- wins listed at the end are not in order (highest win first)
- wins listed at the end contain bonus number matches even when it doesn't matter (3+1 correct or 4+1 correct, when the correct bonus number doesn't increase the win)
