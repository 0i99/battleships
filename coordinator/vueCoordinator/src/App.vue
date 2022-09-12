<template>
<div class="container" style="max-width: 1000px">

  <ul class="nav nav-tabs">
    <li class="nav-item">
        <router-link to="/" class="nav-link" active-class="active">Game</router-link> 
    </li>
    <li class="nav-item">
      <router-link to="/history" class="nav-link" exact-active-class="exact-active">History</router-link>
    </li>
  </ul>

  <br/>
  <router-view/>
</div>
 
</template>



