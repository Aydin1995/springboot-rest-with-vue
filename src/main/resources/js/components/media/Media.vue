<template>
    <v-card>
        <v-flex v-if="message.type==='href'" xs12 sm6 offset-sm3>
            <v-img v-if="message.linkCover" :src="message.linkCover" aspect-ratio="2.75"></v-img>
  <v-card-title>
      <div>
          <h3>
              <a :href="message.link">{{message.linkTitle || message.link}}</a>
          </h3>
          <div v-if="message.linkDescription">{{message.linkDescription}}</div>
      </div>

  </v-card-title>
        </v-flex>
        <v-flex v-if="message.type==='image'" xs12 sm6 offset-sm3>
            <a :href="message.link">
            <v-img v-if="message.linkCover" :src="message.linkCover" aspect-ratio="2.75"></v-img>
                {{message.link}}
            </a>
        </v-flex>
        <v-flex v-if="message.type==='youtube'" xs12 sm6 offset-sm3>
            <youtube :src="message.link"></youtube>
        </v-flex>
    </v-card>
</template>

<script>
    import Youtube from './Videos.vue'
    export default {
        name: "Media",
        components:{ Youtube },
        props:['message','link'],
        data(){
            return{
                type:'href'
            }
        }
       //  ,
       //
       //
       // beforeMount() {
       //      if(this.message.link.indexOf('youtu')>-1){
       //          console.log(this.link);
       //          this.type='youtube'
       //      }
       //      else if (this.message.link.match(/\.(jpeg|jpg|gif|png)$/) !== null) {
       //          this.type = 'image'
       //          console.log(this.type)
       //      } else {
       //          this.type = 'href'
       //          console.log(this.type)
       //      }
       //  }
    }
</script>

<style scoped>

</style>