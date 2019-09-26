<template>
    <v-container>
    <v-layout align-space-around justify-start column>
        <message-form :messageAttr="message" />
        <message-row v-for="(message,index) in sortedMessages"
                     :key="`message.id-${index}`"
                     :message="message"
                     :imageUrl="message.file"
                     :editMessage="editMessage" />
        <lazy-loader></lazy-loader>
    </v-layout>
    </v-container>
</template>

<script>
    import { mapGetters } from 'vuex'
    import MessageRow from 'components/messages/MessageRow.vue'
    import MessageForm from 'components/messages/MessageForm.vue'
    import LazyLoader from '../components/LazyLoader.vue'
    export default {
        components: {
            LazyLoader,
            MessageRow,
            MessageForm
        },
        data() {
            return {
                message: null
            }
        },
        computed: mapGetters(['sortedMessages']),
        methods: {
            editMessage(message) {
                this.message = message
            }
        }
    }
</script>

<style>
</style>