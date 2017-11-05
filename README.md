# dvr-calculator
Distance Vector Routing (DVR) protocol simulator and routing table generator. 

Loads topology files and generates routing tables for each router. Uses relaxation via the Bellman-Ford algorithm to scan the graph as many times as there are nodes present. Based off lab work done in a network applications course at university.

#### Features (existing and planned):
- [x] Load formatted topology files and display graph on screen
- [x] Calculate initial DVR table
- [x] Fill table using DVR protocol and update initial tables
- [x] Basic route finding mechanism using the generated tables
- [ ] Interactive graph showing individual tables for each node
- [ ] Table highlighting to show best route
- [ ] Updated route finding (link state)
- [ ] Standardised file formatting (e.g. XML, JSON)
