# Vue Coordinator

## Create project
type 

```
vue create vueCoordinator
```
and select options


## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Build and run from docker file
```
docker build -t battleships/vue-coordinator .
docker run -it -p 8080:80 --rm --name vue-coordinator-1 battleships/vue-coordinator
```



