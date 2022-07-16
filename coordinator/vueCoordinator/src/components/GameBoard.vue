
<template>
    <table class="board" >
      <thead>
        <tr><th :colspan="size+1" class="header" :class="team">{{team.toUpperCase()}} team <span v-if="url">({{url}})</span> <span v-if="id" >, game: {{id}}</span> </th></tr>
      </thead>
      <tbody>
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
  

      <!-- <tr v-for="row in (1,size)" :key="row">
        <td>{{row}}</td>
        <td v-for="col in (1,size)" :key="col" >
          
        </td>
      </tr> -->
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
    team: String,
    id: String,
    url: String,
    positions: Array
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
.board td{
  border: 1px solid #eee;
  border-radius: 2px;

  margin: 2px;
  width: 20px;
  height: 20px;
  text-align: center;
}
.header{
  color: #fff;
  padding: 5px;
}
.red {
  background: #d8031c;
}
.blue {
  background: #9fcbee;
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
</style>
