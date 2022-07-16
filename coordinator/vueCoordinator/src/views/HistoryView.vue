<template>
  <b-container fluid>
    <b-row>
      <b-col sm="2">
        <b-form-input v-model="gameId" placeholder="type gameId" />
      </b-col>
      <b-col sm="2">
        <b-button variant="outline-info" href="#" @click="refresh()">GET RESULTS</b-button>
      </b-col>
    </b-row>
  </b-container>
  <br />
  
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
  name: 'HistoryView',
  components: {
    GameBoard
  },
  data() {
    return {
      gameId: "",
      size: parseInt(process.env.VUE_APP_SIZE),
      blueTeamUrl: process.env.VUE_APP_BLUE_TEAM_URL,
      redTeamUrl: process.env.VUE_APP_RED_TEAM_URL,
      bluePositions: [],
      redPositions: [],
      errorMessage: ""
    }
  },
  mounted() {
    this.bluePositions=this.emptyGameBoard();
    this.redPositions=this.emptyGameBoard();
  },

  methods: {
    emptyGameBoard(){
      return new Array(this.size).fill(0).map(() => new Array(this.size).fill(0))
    },
    async refresh() {
      this.bluePositions=this.emptyGameBoard();
      this.redPositions=this.emptyGameBoard();

      const getShotsOptions = {
          method: "GET",
          headers: {
            "Accept": "application/json",
            "Content-Type": "application/json",
          },
          mode: "cors"
      };
      fetch(this.blueTeamUrl + "/game/"+this.gameId+"/shot",getShotsOptions)
        .then(async response => {
          const data = await response.json();
          data.map(val => {
            const newRow = this.bluePositions[val.x].slice(0);
            newRow[val.y] = val.hit ? -1 : 1;
            this.bluePositions[val.x]=newRow;
          });
          this.errorMessage = "";
      }).catch(error => {
        this.errorMessage = error;
        console.error("There was an error!", error);
      });

      fetch(this.redTeamUrl + "/game/"+this.gameId+"/shot",getShotsOptions)
        .then(async response => {
          const data = await response.json();
          data.map(val => {
            const newRow = this.redPositions[val.x].slice(0);
            newRow[val.y] = val.hit ? -1 : 1;
            this.redPositions[val.x]=newRow;
          });
          this.errorMessage = "";
      }).catch(error => {
        this.errorMessage = error;
        console.error("There was an error!", error);
      });

    

    }
  }
}
</script>