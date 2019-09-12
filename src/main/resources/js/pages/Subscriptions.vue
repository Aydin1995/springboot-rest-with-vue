<template>
    <v-container>
        <v-layout justify-space-around>
         <v-list>
             <v-list-tile
             v-for="item in subscriptions">
                 <user-link :user="item.subscriber"
                             size="24">

                 </user-link>
                 <v-btn @click="changeSubscriptionStatus(item.subscriber.id)">
                     {{item.active?"Dismiss":"Approve"}}
                 </v-btn>
             </v-list-tile>
         </v-list>
        </v-layout>
    </v-container>
</template>

<script>
    import profileApi from 'api/profile'
    import UserLink from 'components/UserLink.vue';
    export default {
        name: "Subscriptions",
        components: {UserLink},
        data(){
            return{
                subscriptions:[]
            }
        },
        methods:{
        async changeSubscriptionStatus(subscriberId){
await profileApi.changeSubscriptionStatus(subscriberId)
           const subscriptionsindex= this.subscriptions.findIndex(i=>i.subscriber.id===subscriberId)
            const  subscription=this.subscriptions[subscriptionsindex]
            this.subscriptions=[
    ...this.subscriptions.slice(0,subscriptionsindex),
    {
        ...subscription,
        active:!subscription.active
    },
    ...this.subscriptions.slice(subscriptionsindex +1)
]
        }
        },
        async beforeMount() {
const resp=await profileApi.subscriberList(this.$store.state.profile.id)
            this.subscriptions=await resp.json()
        }
    }
</script>

<style scoped>

</style>