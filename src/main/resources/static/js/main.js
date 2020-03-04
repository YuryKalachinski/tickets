function getIndex(list, id) {
    for (var i=0; i<list.length; i++) {
        if (list[i].id === id) {
            return i;
        }
    }
    return -1;
}

const registrationApi =  Vue.resource('/registry');

const userApi =  Vue.resource('/user{/id}');

Vue.component('registration', {

    data: function() {
        return {
            user: null,
            switch1: false,
            valid: true,
            login: '',
            loginRules: [
                v => !!v || 'Login is required',
                v => (v && v.length <= 20) || 'Login must be less than 20 characters',
            ],
            password: '',
            passwordRules: [
                v => !!v || 'Password is required',
                v => (v && v.length>=3)  || 'Password must be more than 3' ,
                v => (v && v.length <= 20) || 'Password must be less than 20 characters',
            ],
            confirmPassword: '',
            confirmPasswordRules: [
                v => (v === this.password) || 'Password and Confirm Password does not match',
            ],
            firstName: '',
            lastName: '',
            switchRules: [
                v => !!v || 'You must agree to continue!',
            ],
        }
    },

    methods: {
        save: function() {
            if(this.$refs.form.validate()) {
                var user = {login: this.login, password: this.password, creationDate: this.creationDate,
                    firstName: this.firstName, lastName: this.lastName, roles: this.roles, state: this.state};
                registrationApi.save(user);
                router.go(-1);
            }
        },
        clearForm: function() {
            this.login = '';
            this.password = '';
            this.confirmPassword = '';
            this.firstName = '';
            this.lastName = '';
        },
        backSpace: function() {
            router.go(-1);
        }
    },

    template:
        ' <v-layout class="ma-10"> ' +
            ' <v-form ' +
                ' ref="form" ' +
                ' v-model="valid" ' +
                ' lazy-validation ' +
            ' > ' +
                ' <v-row> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="login" ' +
                            ' :counter="20" ' +
                            ' :rules="loginRules" ' +
                            ' label="Login" ' +
                            ' required ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="password" ' +
                            ' :counter="20" ' +
                            ' :rules="passwordRules" ' +
                            ' label="Password" ' +
                            ' required ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="confirmPassword" ' +
                            ' :counter="20" ' +
                            ' :rules="confirmPasswordRules" ' +
                            ' label="Confirm Password" ' +
                            ' required ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="firstName" ' +
                            ' :counter="20" ' +
                            ' label="First Name" ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="lastName" ' +
                            ' :counter="30" ' +
                            ' label="Last Name" ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                ' </v-row> ' +
                ' <v-switch ' +
                    ' v-model="switch1" ' +
                    ' label="Do you agree?" ' +
                    ' :rules="switchRules" ' +
                ' ></v-switch> ' +
                ' <v-btn ' +
                    ' :disabled="!valid " ' +
                    ' color="success" ' +
                    ' class="mr-4" ' +
                    ' @click="save" ' +
                    ' > ' +
                    ' Registration ' +
                ' </v-btn> ' +
                ' <v-btn ' +
                    ' color="lime" ' +
                    ' class="mr-4" ' +
                    ' @click="clearForm" ' +
                    ' > ' +
                    ' Clear ' +
                ' </v-btn> ' +
                ' <v-btn ' +
                    ' color="cyan" ' +
                    ' class="mr-4" ' +
                    ' @click="backSpace" ' +
                    ' > ' +
                    ' Back ' +
                ' </v-btn> ' +
                '</v-row>' +
            ' </v-form> ' +
        ' </v-layout>',
});

Vue.component('user', {

    data: function() {
        return {
            user: null,
            switch1: false,
            valid: true,
            id: '',
            login: '',
            loginRules: [
                v => !!v || 'Login is required',
                v => (v && v.length <= 20) || 'Login must be less than 20 characters',
            ],
            password: '',
            passwordRules: [
                v => !!v || 'Password is required',
                v => (v && v.length>=3)  || 'Password must be more than 3' ,
                v => (v && v.length <= 20) || 'Password must be less than 20 characters',
            ],
            confirmPassword: '',
            confirmPasswordRules: [
                v => (v === this.password) || 'Password and Confirm Password does not match',
            ],
            firstName: '',
            lastName: '',
            roles: [],
            rolesRules: [
                v => !!v || 'Roles is required',
            ],
            rolesOf: ['USER', 'ADMIN'],
            state: '',
            statusRules: [
                v => !!v || 'Status is required',
            ],
            stateOf: ['ACTIVE', 'BANNED', 'DELETED'],
            creationDate: '',
            switchRules: [
                v => !!v || 'You must agree to continue!',
            ],
        }
    },

    mounted: function() {
        userId = this.$route.params.id;
        if(userId !== 'newPerson') {
            userApi.get({id: userId}).then(result=>
                result.json().then(data => {
                    this.user = data;
                })
            )
        }
    },

    watch: {
        user: function(newVal, oldVal) {
            this.login = newVal.login;
            this.password = '';
            this.confirmPassword = '';
            this.firstName = newVal.firstName;
            this.lastName = newVal.lastName;
            this.id = newVal.id;
            this.roles = newVal.roles;
            this.state = newVal.state;
            this.creationDate = newVal.creationDate;
        }
    },

    methods: {
        save: function() {
            if(this.$refs.form.validate()) {
                if(this.id) {
                    var userToDB = {id: this.id, login: this.login, password: this.password, creationDate: this.creationDate,
                        firstName: this.firstName, lastName: this.lastName, roles: this.roles, state: this.state};
                    userApi.update({id: this.id}, userToDB);
                    router.go(-1);
//                    router.push({ path: `/person` });
//                    router.push({ path: `/user` });
                } else {
                    var user = {login: this.login, password: this.password, creationDate: this.creationDate,
                        firstName: this.firstName, lastName: this.lastName, roles: this.roles, state: this.state};
                    userApi.save(user);
                    router.go(-1);
//                    router.push({ path: `/person` });
//                    router.push({ path: `/user` });
                }
            }
        },
        clearForm: function() {
            this.login = '';
            this.password = '';
            this.confirmPassword = '';
            this.firstName = '';
            this.lastName = '';
            this.roles = [];
            this.state = '';
        },
        backSpace: function() {
            router.replace({ path: `/person` });
        }
    },

    template:
        ' <v-layout class="ma-10"> ' +
            ' <v-form ' +
                ' ref="form" ' +
                ' v-model="valid" ' +
                ' lazy-validation ' +
            ' > ' +
                ' <v-row> ' +
                    ' <v-col cols="12" md="1"> ' +
                        ' <v-text-field ' +
                            ' v-model="id" ' +
                            ' label="ID" ' +
                            ' disabled ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="login" ' +
                            ' :counter="20" ' +
                            ' :rules="loginRules" ' +
                            ' label="Login" ' +
                            ' required ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="password" ' +
                            ' :counter="20" ' +
                            ' :rules="passwordRules" ' +
                            ' label="Password" ' +
                            ' required ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="confirmPassword" ' +
                            ' :counter="20" ' +
                            ' :rules="confirmPasswordRules" ' +
                            ' label="Confirm Password" ' +
                            ' required ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="firstName" ' +
                            ' :counter="20" ' +
                            ' label="First Name" ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="lastName" ' +
                            ' :counter="30" ' +
                            ' label="Last Name" ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-select ' +
                            ' v-model="roles" ' +
                            ' :multiple="true" ' +
                            ' :items="rolesOf" ' +
                            ' :rules="rolesRules" ' +
                            ' label="Roles" ' +
                            ' required ' +
                        ' ></v-select> ' +
                    ' </v-col>' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-text-field ' +
                            ' v-model="creationDate" ' +
                            ' :counter="20" ' +
                            ' label="Date of Creation" ' +
                            ' disabled ' +
                        ' ></v-text-field> ' +
                    ' </v-col> ' +
                    ' <v-col cols="12" md="4"> ' +
                        ' <v-select ' +
                            ' v-model="state" ' +
                            ' :items="stateOf" ' +
                            ' :rules="statusRules" ' +
                            ' label="Status" ' +
                            ' required ' +
                        ' ></v-select> ' +
                    ' </v-col>' +
                ' </v-row> ' +
                ' <v-switch ' +
                    ' v-model="switch1" ' +
                    ' label="Do you agree?" ' +
                    ' :rules="switchRules" ' +
                ' ></v-switch> ' +
                ' <v-btn ' +
                    ' :disabled="!valid " ' +
                    ' color="success" ' +
                    ' class="mr-4" ' +
                    ' @click="save" ' +
                    ' > ' +
                    ' Save ' +
                ' </v-btn> ' +
                ' <v-btn ' +
                    ' color="lime" ' +
                    ' class="mr-4" ' +
                    ' @click="clearForm" ' +
                    ' > ' +
                    ' Clear ' +
                ' </v-btn> ' +
                ' <v-btn ' +
                    ' color="cyan" ' +
                    ' class="mr-4" ' +
                    ' @click="backSpace" ' +
                    ' > ' +
                    ' Back ' +
                ' </v-btn> ' +
                '</v-row>' +
            ' </v-form> ' +
        ' </v-layout>',
});

Vue.component('users-list',{

    data: function() {
        return {
            users: [],
            search: '',
            headers: [{
                text: 'ID',
                align: 'left',
                sortable: false,
                value: 'id',
            },
            { text: 'Login', value: 'login' },
            { text: 'First Name', value: 'firstName' },
            { text: 'Last Name', value: 'lastName' },
            { text: 'Roles', value: 'roles' },
            { text: 'Date of Creation', value: 'creationDate' },
            { text: 'Status', value: 'state' },
            { text: 'Actions', value: 'action', sortable: false },
            ]
        }
    },

    mounted: function() {
        this.getUsers();
    },

    beforeUpdate: function() {
        this.getUsers();
    },

    methods: {
        getUsers: function() {
            userApi.get().then(result=>
                result.json().then(data => {
                    this.users = data;
                })
            )
        },

        editItem: function(item){
            userId = item.id,
            router.push({ path: `/person/${userId}` })
//            router.push({ path: `/user/${userId}` })
        },
        deleteItem: function(item){
            userId = item.id,
            userApi.remove({id: userId}).then(result=>{
                if(result.ok) {
                    this.users.splice(this.users.indexOf(item), 1)
                }
            })
        },
        createItem: function(){
            router.push({ path: `/person/newPerson` })
//            router.push({ path: `/user/newUser` })
        }
    },

    template:
        ' <v-card> ' +
            ' <v-card-title> ' +
                ' <v-layout> ' +
                    ' Users ' +
                ' </v-layout> ' +
                ' <v-spacer></v-spacer> ' +
                ' <v-text-field ' +
                    ' v-model="search" ' +
                    ' append-icon="search" ' +
                    ' label="Search" ' +
                    ' single-line ' +
                    ' hide-details ' +
                ' ></v-text-field> ' +
                ' <v-spacer></v-spacer> ' +
                    ' <v-icon class="mr-10 mt-6" ' +
                        ' @click="createItem" > person_add ' +
                    ' </v-icon> ' +
            ' </v-card-title> ' +
            ' <v-data-table ' +
                ' :headers="headers" ' +
                ' :items="users" ' +
                ' :search="search" ' +
                ' :items-per-page="5" ' +
                ' class="elevation-1" ' +
            ' > ' +
                ' <template v-slot:item.action="{ item }"> ' +
                    ' <v-icon small class="mr-4" ' +
                        ' @click="editItem(item)" > edit ' +
                    ' </v-icon> ' +
                    ' <v-icon small class="ml-4"' +
                        ' @click="deleteItem(item)" > delete ' +
                    ' </v-icon> ' +
                ' </template> ' +
            ' </v-data-table> ' +
        ' </v-card> ' ,

});

const Registration = {
    template:
        ' <registration></registration> ' ,
}

const Home = {
    template:
        '<div>' +
            'Home' +
        '</div>' ,
}

const User = {
    template:
        ' <user></user> ' ,
}

const Users = {
    template:
        '<users-list></users-list>' ,
}

const Location = {
    template:
        '<div>' +
            'Location'  +
        '</div>' ,

}

const Ticket = {
    template:
        '<div>' +
            'Ticket'    +
        '</div>' ,
}

const Event = {
    template:
        '<div>' +
            'Event'   +
        '</div>' ,
}

const router = new VueRouter({
    mode: 'history',
    routes: [
        { path: '/registration', component: Registration},
        { path: '/person/newPerson', component: User },
        { path: '/person/:id', component: User },
        { path: '/person', component: Users, },
        { path: '/location', component: Location },
        { path: '/ticket', component: Ticket },
        { path: '/event', component: Event },
        { path: '/', component: Home },
        { path: '*', component: Home },
    ]
})

const app = new Vue({
    el: '#app',
    vuetify: new Vuetify(),
    router,
    data: {
        profile: frontEndData.profile
    },
    methods: {
        isAdmin: function () {
            for (var i=0; i<this.profile.authorities.length; i++ ) {
                if (this.profile.authorities[i] === "ADMIN") {
                    return true;
                }
            }
            return false;
        }
    },
})