<template>
    <v-container>
        <v-layout justify-space-around>
            <v-list>
                <v-list-tile
                    v-for="item in allusers">

                    <user-link class="stili" :user="item"
                                size="24">

                    </user-link>

                </v-list-tile>
            </v-list>
        </v-layout>
    </v-container>
</template>

<script>
    import { mapState } from 'vuex'
    import UserLink from '../components/UserLink.vue'

    export default {
        name: "AllUsers",
        components: {UserLink},
        computed: mapState(['allusers']),


    }
</script>


<style scoped>
.stili{
color:rebeccapurple;
}

</style>