function getIndex(list, id) {
    for (var i =0; i<list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

var userApi =  Vue.resource('/user{/id}');

Vue.component('user-form', {
    props:['users', 'userAttr'],
    data: function() {
        return {
            text: '',
            id: ''
        }
    },
    watch: {
        userAttr: function(newVal, oldVal) {
            this.text = newVal.login;
            this.id = newVal.id;
        }
    },
    template:
        '<div>' +
            '<input type="text" placeholder="Write something" v-model="text" />' +
            '<input type="button" value="Save" @click="save" />'+
        '</div>',
     methods: {
        save: function() {

            if(this.id) {
                var user = {id: this.id, login: this.text, password: '123'};
                userApi.update({id: this.id}, user).then(result=>
                    result.json().then(data => {
                        var index = getIndex(this.users, data.id);
                        this.users.splice(index , 1, data);
                        this.text='';
                        this.id = '';
                    })
                )
            } else {
                var user = { login: this.text, password: '123'};
                userApi.save(user).then(result=>
                    result.json().then(data => {
                        this.users.push(data);
                        console.log(data);
                        this.text='';
                    })
                )
            }
        }
     }
});

Vue.component('user-row',{
    props: ['user', 'editMethod', "users"],
    template:'<div>' +
                '{{ user.id }} {{user.login}} ' +
                '<span style="position: absolute; right: 0">' +
                    '<input type ="button" value="Edit" @click="edit" />' +
                    '<input type ="button" value="X" @click="del" />' +
                '</span>' +
              '</div>',
    methods: {
        edit: function(){
            this.editMethod(this.user)
        },
        del: function(){
            userApi.remove({id: this.user.id}).then(result=>{
                if(result.ok) {
                    this.users.splice(this.users.indexOf(this.user), 1)
                }
            })
        }
    }
});

Vue.component('user-list',{
    props: ['users'],
    data: function() {
        return{
            user: null
        }
    },
    template:
        '<div style="position: relative; width: 300px">' +
            '<user-form :users="users" :userAttr ="user" /> ' +
            '<user-row v-for="user in users" :key="user.id" ' +
            ':user="user" :editMethod="editMethod" :users="users"/>' +
         '</div>',
    methods: {
        editMethod: function(user) {
            this.user = user;
        }
    }
});

var app = new Vue({
  el: '#app',
  template:'<user-list :users="users" />',
  data: {
    users:[]
  },
  created: function(){
          userApi.get().then(result=>
              result.json().then(data=> {
                  data.forEach(user=>this.users.push(user));
                  console.log(data);
              })
          )
  }
});