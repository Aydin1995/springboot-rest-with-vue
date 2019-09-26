<template>
    <v-layout row>
        <v-text-field
                label="New message"
                placeholder="Write something"
                v-model="text"
                @keyup.enter="save"
        />
        <v-btn type="file" icon @click="selectafile">
            <v-icon>party_mode</v-icon>
        </v-btn>
        <input type="file" style="display: none" ref="fileInput" accept="image/*" @change="onFilePicked">
        <v-btn @click="save">
            Save
        </v-btn>


    </v-layout>
</template>

<script>
    import { mapActions } from 'vuex'
    export default {
        props: ['messageAttr'],
        data() {
            return {

                text: '',
                id:null,
                file:null


            }
        },
        watch: {
            messageAttr(newVal, oldVal) {
                this.text = newVal.text
                this.id = newVal.id
            }
        },
        methods: {
            ...mapActions(['addMessageAction', 'updateMessageAction']),
            save() {
                const messag= {

  message:{

      text: this.text,
      id: this.id


  },

                    file: this.file

                }
                if (this.id) {

                    this.updateMessageAction(messag)
                } else {
                    this.addMessageAction(messag)

                }

                this.text = ''
                this.id = null;
                this.file=null;

            },
            selectafile(){
this.$refs.fileInput.click();
            },
            onFilePicked(event){
 const files=event.target.files
                let filename=files[0].name;
if(filename.lastIndexOf('.')<=0){
    return alert('please add a valid file!')

}
else{

    const uuidv4 = require('uuid/v4');



    this.file=files[0]
    this.filename=files[0].name
}
// const filereader=new FileReader()
//                 filereader.addEventListener('load',()=>{
//
//
//
//
//                 })
//                 filereader.readAsDataURL(files[0])
//                 this.image=files[0]
//               ;
            }
        }
    }
</script>

<style>
</style>