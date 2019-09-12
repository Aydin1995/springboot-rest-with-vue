<template>
    <iframe
            width="560" height="315"
            :src="targetSrc"
            frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture"
            allowfullscreen>

    </iframe>
</template>

<script>
    export default {
        name: 'Youtube',
        props:['src'],
        computed:{
            targetSrc(){
                if(this.src.includes("youtube")) {
               const subs  =     this.src.substring(this.src.indexOf("=")+1);
                    this.src = "https://youtu.be/" + subs;
                }
            let parts = this.src.split('/')

                return `https://www.youtube.com/embed/${parts[parts.length - 1]}`

            }

        }


    }
</script>

<style scoped>

</style>