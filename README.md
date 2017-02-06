# CS499_Assignment2

<b>Entities and Relationships:</b><br><br>
<b>My entities:</b> are Sport, Coach, Player, Quote and Background.<br><br>
<b>My relationships:</b><br>
&nbsp;&nbsp;&nbsp;&nbsp;OneToMany: <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sport{typeofCoach} to Coach, <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Sport{name} to Player <br>
&nbsp;&nbsp;&nbsp;&nbsp;OneToOne. <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Player{stats} to Statistics, <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Player{quote} to Quote, <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Background{coach} to Coach, <br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Background{player} to Player <br>

<b>Description</b>:<br>
<p>The topic I picked was sports. I have an entity that holds all categories of sports.<br>
   Also I have a background information that will relate to the coaches/players of a team.<br>
   I want to note that any amount of coaches/players can have the background information.<br>
   A player can have more data(relationship) on the Statistics and Quote Entities.<br> 
   Statistics holds information about Wins, Losses, and Draws.<br>
   Quote is just a special phrase that a player lives by everyday.</p><br>

<b>URL<b/>: http://ec2-54-153-64-127.us-west-1.compute.amazonaws.com:8080 <br>

