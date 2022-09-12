<template>
  <!-- <b-container fluid>
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
  </div> -->
  TODO
</template>


<script>
// @ is an alias to /src


export default {
  name: 'HistoryView',
  components: {

  },
  data() {
    return {
      gameId: "",
      size: parseInt(process.env.VUE_APP_SIZE),
    
      errorMessage: ""
    }
  },
  mounted() {

  },

  methods: {
  

    
  }
}
</script>