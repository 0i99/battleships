
<template>
   
    <table class="board" >
      <thead>
        <tr>
          <th :colspan="size+1" class="header" >
              <span v-for="it in stack" :key="it">
                <img v-bind:src="require(`../assets/${it}.png`)" class="rounded h-25 w-25"/>
              </span>
              <img v-if="winner" v-bind:src="require(`../assets/winner.png`)" class="rounded h-25 w-25 trophy" />
              
          </th>
        </tr>
      </thead>
      <tbody class="winner">
      <tr>
        <td></td>
        <td :colspan="size"><span v-if="url">url: {{url}}</span> <span v-if="id" >game: {{id}}</span></td>
      </tr>
      <tr>
        <td></td>
        <td v-for="(rowa, idxa1) in positions" :key="rowa" >{{idxa1}}</td>
      </tr>

      <tr v-for="(row, idx1) in positions" :key="row">
        <td>{{idx1}}</td>
        <td v-for="(col, idx2) in row" :key="col">                     
         <span v-bind:class="'position'+positions[idx1][idx2]">x</span>
        </td>
      </tr>

      </tbody>
    </table>
</template>

<script>


export default {
  name: 'GameBoard',
  props: {
    size: {
      type: Number,
      default: 10
      },
    id: String,
    url: String,
    positions: Array,
    stack: Array,
    winner: {
      type: Boolean,
      default: false
      }
  },
  methods : {
     
  }
}

</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.board{
  font-size: x-small;
}
.winner{
  
}
.board td{
  border: 1px solid #eee;
  border-radius: 2px;

  margin: 2px;
  width: 20px;
  height: 20px;
  text-align: center;
}
.header{
  color: #333;
  padding: 5px;
  font-size: small;
}

.position0{
  display: none;
}
.position-1{
  display: inline-block;
  width: 100%;
  height: 100%;
  background-color: black;
  color: white
}
.position1{
  display: inline-block;
  width: 100%;
  height: 100%;
}
.trophy{
  margin-left: 10px;
}
</style>
