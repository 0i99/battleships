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
   
  <div class="row">
    <div class="col">
      <GameBoard :size="size" :team='"blue"' :id="gameId" :url="blueTeamUrl" :positions="bluePositions"/>
    </div>
    <div class="col">
      <GameBoard :size="size" :team='"red"' :id="gameId" :url="redTeamUrl" :positions="redPositions"/>
    </div>
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
      data: "",
      gameId: "",
      size: parseInt(process.env.VUE_APP_SIZE),
      blueTeamUrl: process.env.VUE_APP_BLUE_TEAM_URL,
      redTeamUrl: process.env.VUE_APP_RED_TEAM_URL,
      bluePositions: [],
      redPositions: [],
      errorMessage: "",
      running : false
    }
  },
  mounted() {
    this.bluePositions=this.emptyGameBoard();
    this.redPositions=this.emptyGameBoard();
  },

  methods: {
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
      this.gameId = Math.ceil(9999999 * Math.random() * 1000000).toString(16);
  
      fetch(this.blueTeamUrl + "/game",{
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
          },
        mode: "cors",
        body: JSON.stringify({
          "id": this.gameId,
          "size": this.size,
          "firstShotIsYours" : true
        })
      })
        .then(async response => {
          const data = await response.json();
          this.errorMessage = "";
          console.log(data);
      }).catch(error => {
        this.errorMessage = "There was an error while starting game for blue team("+this.blueTeamUrl+")" + error;
      });


      fetch(this.redTeamUrl + "/game",{
        method: "POST",
        headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
          },
        mode: "cors",
        body: JSON.stringify({
          "id": this.gameId,
          "size": this.size,
          "firstShotIsYours" : false
        })
      })
        .then(async response => {
          const data = await response.json();
          this.errorMessage = "";
          console.log(data);
      }).catch(error => {
        this.errorMessage = "There was an error while starting game for red team("+this.redTeamUrl+")" + error;
      });

      this.running = true;
      window.setInterval(() => {
                this.refresh();
                this.getGameStatus();
            },1000);
    },


    /**
     * 
     */
    async refresh() {
      if(this.running){
        this.bluePositions=this.emptyGameBoard();
        this.redPositions=this.emptyGameBoard();

        const request = {
            method: "GET",
            headers: {
              "Accept": "application/json",
              "Content-Type": "application/json",
            },
            mode: "cors"
        };
        fetch(this.blueTeamUrl + "/game/"+this.gameId+"/shot",request)
          .then(async response => {
            const data = await response.json();
            data.map(val => {
              const newRow = this.bluePositions[val.x].slice(0);
              newRow[val.y] = val.hit ? -1 : 1;
              this.bluePositions[val.x]=newRow;
            });
            this.errorMessage = "";
        }).catch(error => {
          this.errorMessage = "There was an error while refreshing game for blue team("+this.blueTeamUrl+")" + error;
        });

        fetch(this.redTeamUrl + "/game/"+this.gameId+"/shot",request)
          .then(async response => {
            // console.log("got " + response.status) 
            // if(response.status == 410){
            //   this.errorMessage = "Game is over"
            //   this.running = false;
            // }
            const data = await response.json();
            data.map(val => {
              const newRow = this.redPositions[val.x].slice(0);
              newRow[val.y] = val.hit ? -1 : 1;
              this.redPositions[val.x]=newRow;
            });
            this.errorMessage = "";
        }).catch(error => {
          this.errorMessage = "There was an error while refreshing game for red team("+this.redTeamUrl+")" + error;
        });
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
        fetch(this.blueTeamUrl + "/game/"+this.gameId,request)
          .then(async response => {
            const data = await response.json();
            if(data == "OVER"){
              this.errorMessage = "Game is over, RED team won"
              console.log("game over RED team won");
              this.running = false;
            }
        }).catch(error => {
          this.errorMessage = "There was an error while getting game status for blue team("+this.blueTeamUrl+")" + error;
        });

        fetch(this.redTeamUrl + "/game/"+this.gameId,request)
          .then(async response => {
            const data = await response.json();
            if(data == "OVER"){
              this.errorMessage = "Game is over, BLUE team won"
              console.log("game over BLUE team won");
              this.running = false;
            }
        }).catch(error => {
          this.errorMessage = "There was an error while getting game status for red team("+this.redTeamUrl+")" + error;
        });
      }
    }
  }
}
</script>

<style>

</style>
