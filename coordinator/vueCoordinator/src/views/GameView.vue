<template>
  <b-container fluid>
    <b-row>
      <b-col sm="2">
         <b-button variant="outline-success" href="#" @click="startGame()" :disabled="running == true">START</b-button> 
      </b-col>
    </b-row>
  </b-container>
  <br/>
  <b-alert show variant="danger" v-if="errorMessage">{{errorMessage}}</b-alert>

  <div v-for="(it, idx1) in playersUrl.split(',')" :key="it" class="boards">
   <div class="board" v-bind:id=idx1>
      <GameBoard :size="size" :id="gameId" :url="it" :positions="playersPositionMap.get(it)" :stack="playersInfoMap.get(it)" :winner="winners.includes(it)"/>
    </div>
    <div class="vs" v-if="(idx1)%2==0">VS</div>   
    <br v-if="(idx1)%2==1" style="clear:both" />   
  </div> 

</template>

<script>
// @ is an alias to /src
import GameBoard from '@/components/GameBoard.vue'

export default {
  name: 'GameView',
  components: {
    GameBoard
  },
  data() {
  
    return {
      gameId: "",
      size: parseInt(process.env.VUE_APP_SIZE),
      playersUrl: process.env.VUE_APP_PLAYERS_URL,
      sleep: process.env.VUE_APP_SLEEP,
      playersInfoMap : new Map() ,
      playersPositionMap : new Map() ,
      opponents : new Map(),
      winners: [],
      errorMessage: "",
      running : false
    }
  },
  mounted() {
    this.loadPositions();
    this.loadPlayers();
  },

  methods: {
    loadPositions(){
      this.playersUrl.split(",").map(x=> { 
        this.playersPositionMap.set(x, this.emptyGameBoard());
      });
    },
    loadPlayers(){
        var players = this.playersUrl.split(",");

        players.map(x=> {
          console.log("Loading player " + x);
          fetch(x + "/information")
          .then(async response => {
            const data = await response.json();
            this.playersInfoMap.set(x, data.details);
          });
        });

        //oponents pairs
        this.opponents = new Map();
        let n = 0;
        while (n++ < players.length) {
          const left = players.pop();
          const right = players.pop();
          this.opponents.set(left, right);
          this.opponents.set(right, left);
        }

    },
    /**
     * Create empty board with size specified in env
     */
    emptyGameBoard(){
      return new Array(this.size).fill(0).map(() => new Array(this.size).fill(0))
    },


    /**
     * 
     */
    async startGame() {
      this.loadPositions();
      this.loadPlayers()
      this.gameId = Math.ceil(9999999 * Math.random() * 1000000).toString(16);

      Array.from(this.playersInfoMap).map(([key,value]) => {
            var even = key.split(":").pop()%2 == true;
            console.log("Starting game " + key + " for " + value + " team, firstShotIsYours " + even);
            fetch(key + "/game",{
              method: "POST",
              headers: {
                  "Accept": "application/json",
                  "Content-Type": "application/json",
                },
              mode: "cors",
              body: JSON.stringify({
                "id": this.gameId,
                "size": this.size,
                "firstShotIsYours" : even 
              })
            })
            .catch(error => {
            this.errorMessage = "There was an error while starting game for "+value+" team" + error;
          })
        }
      );


      this.running = true;
      window.setInterval(() => {
                this.refresh();
                this.getGameStatus();
            },this.sleep);
    },  


    /**
     * 
     */
    async refresh() {
      if(this.running){
        
        const request = {
            method: "GET",
            headers: {
              "Accept": "application/json",
              "Content-Type": "application/json",
            },
            mode: "cors"
        };
        Array.from(this.playersInfoMap).map(([key,value]) => {
            fetch(key + "/game/"+this.gameId+"/shot",request)
              .then(async response => {
                const data = await response.json();
                data.map(val => {
                  const newRow = this.playersPositionMap.get(key)[val.x].slice(0);
                  newRow[val.y] = val.hit ? -1 : 1;
                  this.playersPositionMap.get(key)[val.x]=newRow;
                });
                this.errorMessage = "";
            }).catch(error => {
              this.errorMessage = "There was an error while getting game status for "+value+" team" + error;
            })
        }
        );

      }
    },


    /**
     * 
     */
    async getGameStatus() {
      if(this.running){
        const request = {
            method: "GET",
            headers: {
              "Accept": "application/json",
              "Content-Type": "application/json",
            },
            mode: "cors"
        };
        Array.from(this.playersInfoMap).map(([key,value]) => {
            fetch(key + "/game/"+this.gameId,request)
              .then(async response => {
                const data = await response.json();
                if(data == "OVER"){
                  let winner = this.opponents.get(key);
                  console.log("Game are over from " + key + ", " + winner + " won");
                  if(!this.winners.includes(winner)){
                    this.winners.push(winner); 
                  }
                  if(this.winners.length==this.playersInfoMap.size/2){
                    console.log("All of games are over")
                    this.running = false;
                  }
                }
            }).catch(error => {
              this.errorMessage = "There was an error while getting game status for "+value+" team" + error;
            })
        });
      }
    }
  }
}
</script>

<style>
.boards div{
  margin-left: 40px;
}
.board{
  margin-bottom: 30px;
  float: left;
}
.vs{
  margin-top: 100px;
  font-size: xx-large;
  float: left;
  color: #333;
}
</style>
