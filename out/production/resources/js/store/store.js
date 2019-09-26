import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from 'api/messages'
import commentApi from 'api/comment'



Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        messages,
        profile,
        allusers,
       ...frontendData

    },

    getters: {
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {

        addMessageMutation(state, message) {


                state.messages = [
                    ...state.messages,
                    message
                ]

            const unique=state.messages

                .reduce((res,val)=>{
                    res[val.id]=val
                    return res
                },{})
            state.messages=Object.values(unique)

        },
        updateMessageMutation(state, message) {

            const updateIndex = state.messages.findIndex(item => item.id === message.id)

            state.messages = [
                ...state.messages.slice(0, updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ]

        },
        removeMessageMutation(state, message) {
            const deletionIndex = state.messages.findIndex(item => item.id === message.id)

            if (deletionIndex > -1) {
                state.messages = [
                    ...state.messages.slice(0, deletionIndex),
                    ...state.messages.slice(deletionIndex + 1)
                ]

            }
        },
        addCommentMutation(stat, comment) {

            const updateIndex = stat.messages.findIndex(item => item.id === comment.message.id)
            const message = stat.messages[updateIndex]

    if (!message.comments.find(it => it.id === comment.id)) {
        stat.messages = [
            ...stat.messages.slice(0, updateIndex),
            {
                ...message,
                comments: [
                    ...message.comments,
                    comment
                ]
            },
            ...stat.messages.slice(updateIndex + 1)
        ]
    }

        },
        addMessagePageMutation(state,messages){
        const targetMessages=state.messages.
        concat(messages)
            .reduce((res,val)=>{
                res[val.id]=val
                return res
            },{})
            state.messages=Object.values(targetMessages)
        },
        updateTotalPagesMutation(state,totalPages){

state.totalPages=totalPages
        },
        updateCurrentPageMutation(state,currentPage){
            state.currentPage=currentPage
        }
    },
    actions: {
        async addMessageAction({commit, state}, message) {
// let data;
//
            console.log(message.file);


            let formData = new FormData();
           formData.append('file', message.file);
           formData.append('text', message.message.text);


//
const result=await Vue.resource('/message').save(formData)
            const data =await result.json();

//
//             // formData.append('id',message.id)
//             // formData.append('text',message.text)
//          axios.post( '/hello',
//
//                 formData,
//
//              {
//                  headers: {
//                      'Content-Type': 'multipart/form-data'
//                  }
//              }
//             )

            // const result = await messagesApi.add(formData)
            // const data = await result.json()


            const index = state.messages.findIndex(item => item.id === data.id)

            if (index > -1) {

                commit('updateMessageMutation', data)
            } else {


                commit('addMessageMutation', data)
            }
        },
        async updateMessageAction({commit}, message) {
            const result = await messagesApi.update(message.message)
            const data = await result.json()
            commit('updateMessageMutation', data)
        },
        async removeMessageAction({commit,dispatch}, message) {
            const result = await messagesApi.remove(message.id)

            if (result.ok) {
                commit('removeMessageMutation', message)
                const el=document.documentElement
                if(window.innerHeight===el.offsetHeight){
                   dispatch('loadPageAction')

                }
            }
        },
        async addCommentAction({commit, state}, comment) {
            const response = await commentApi.add(comment)
            const data = await response.json()
            commit('addCommentMutation', data)
        },
        async loadPageAction({commit,state}){

const response= await messagesApi.page(state.currentPage+1)
            const data= await response.json()



            commit('addMessagePageMutation',data.messages)
            commit('updateTotalPagesMutation',data.totalPages)
            commit('updateCurrentPageMutation',Math.min(data.currentPage,data.totalPages))
        }
    }
})