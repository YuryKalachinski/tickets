function isAdmin() {
    if (frontEndData.profile.authorities.includes('ADMIN')) {
        return true;
    }
}

const authUserApi = Vue.resource('/auth/user');

const registrationApi =  Vue.resource('/checkIn');

const userApi =  Vue.resource('/user{/id}');

const placeApi = Vue.resource('/location{/id}');

const showApi = Vue.resource('/event{/id}');

const cardApi = Vue.resource('/ticket{/id}');

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
                v => (v && v.length >= 3)  || 'Password must be more than 3' ,
                v => (v && v.length <= 20) || 'Password must be less than 20 characters',
            ],
            confirmPassword: '',
            confirmPasswordRules: [
                v => (v == this.password) || 'Password and Confirm Password does not match',
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
                registrationApi.save(user).then(result => {
                    router.push({ path: `/` });
                }, response => {
                    console.log(response);
                })
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
        ` <v-layout class="ma-10">
            <v-form
                ref="form"
                v-model="valid"
                lazy-validation>
                <v-row>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="login"
                            :counter="20"
                            :rules="loginRules"
                            label="Login"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="password"
                            :counter="20"
                            :rules="passwordRules"
                            label="Password"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="confirmPassword"
                            :counter="20"
                            :rules="confirmPasswordRules"
                            label="Confirm Password"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="firstName"
                            :counter="20"
                            label="First Name"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="lastName"
                            :counter="30"
                            label="Last Name"
                        ></v-text-field>
                    </v-col>
                </v-row>
                <v-switch
                    v-model="switch1"
                    label="Do you agree?"
                    :rules="switchRules"
                ></v-switch>
                <v-btn
                    :disabled="!valid "
                    color="success"
                    class="mr-4"
                    @click="save"
                    >
                    Registration
                </v-btn>
                <v-btn
                    color="lime"
                    class="mr-4"
                    @click="clearForm"
                    >
                    Clear
                </v-btn>
                <v-btn
                    color="cyan"
                    class="mr-4"
                    @click="backSpace"
                    >
                    Back
                </v-btn>
                </v-row>
            </v-form>
        </v-layout> `
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
                v => (v && v.length >= 3)  || 'Password must be more than 3' ,
                v => (v && v.length <= 20) || 'Password must be less than 20 characters',
            ],
            confirmPassword: '',
            confirmPasswordRules: [
                v => (v == this.password) || 'Password and Confirm Password does not match',
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
        if(userId != 'newPerson') {
            userApi.get({id: userId}).then(result=> {
                result.json().then(data => {
                    this.user = data;
                })
                }, response => {
                    console.log(response);
                }
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
                    var userToDB = {id: this.id, login: this.login, password: this.password,
                        firstName: this.firstName, lastName: this.lastName, roles: this.roles, state: this.state};
                    userApi.update({id: this.id}, userToDB).then(result=> {
                        router.go(-1);
                    }, response => {
                        console.log(response);
                    })
                } else {
                    var userToDB = {login: this.login, password: this.password, creationDate: this.creationDate,
                        firstName: this.firstName, lastName: this.lastName, roles: this.roles, state: this.state};
                    userApi.save(userToDB).then(result=> {
                        router.go(-1);
                    }, response => {
                        console.log(response);
                    })
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
            router.go(-1);
        }
    },

    template:
        ` <v-layout class="ma-10">
            <v-form
                ref="form"
                v-model="valid"
                lazy-validation
            >
                <v-row>
                    <v-col cols="12" md="1">
                        <v-text-field
                            v-model="id"
                            label="ID"
                            disabled
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="login"
                            :counter="20"
                            :rules="loginRules"
                            label="Login"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="password"
                            :counter="20"
                            :rules="passwordRules"
                            label="Password"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="confirmPassword"
                            :counter="20"
                            :rules="confirmPasswordRules"
                            label="Confirm Password"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="firstName"
                            :counter="20"
                            label="First Name"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="lastName"
                            :counter="30"
                            label="Last Name"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-select
                            v-model="roles"
                            :multiple="true"
                            :items="rolesOf"
                            :rules="rolesRules"
                            label="Roles"
                            required
                        ></v-select>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="creationDate"
                            :counter="20"
                            label="Date of Creation"
                            disabled
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-select
                            v-model="state"
                            :items="stateOf"
                            :rules="statusRules"
                            label="Status"
                            required
                        ></v-select>
                    </v-col>
                </v-row>
                <v-switch
                    v-model="switch1"
                    label="Do you agree?"
                    :rules="switchRules"
                ></v-switch>
                <v-btn
                    :disabled="!valid "
                    color="success"
                    class="mr-4"
                    @click="save"
                >
                    Save
                </v-btn>
                <v-btn
                    color="lime"
                    class="mr-4"
                    @click="clearForm"
                >
                    Clear
                </v-btn>
                <v-btn
                    color="cyan"
                    class="mr-4"
                    @click="backSpace"
                >
                    Back
                </v-btn>
                </v-row>
            </v-form>
        </v-layout> `
});

Vue.component('users-list', {

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

    methods: {
        getUsers: function() {
            userApi.get().then(result=> {
                result.json().then(data => {
                    this.users = data;
                })
            }, response => {
                console.log(response);
            })
        },

        editItem: function(item){
            userId = item.id,
            router.push({ path: `/person/${userId}` })
        },
        deleteItem: function(item){
            userId = item.id,
            userApi.remove({id: userId}).then(result=>{
                if(result.ok) {
                    this.users.splice(this.users.indexOf(item), 1)
                }
            }, response => {
                console.log(response);
            })
        },
        createItem: function(){
            router.push({ path: `/person/newPerson` })
        }
    },

    template:
        ` <v-card>
            <v-card-title>
                <v-layout>
                    Users
                </v-layout>
                <v-spacer></v-spacer>
                <v-text-field
                    v-model="search"
                    append-icon="search"
                    label="Search"
                    single-line
                    hide-details
                ></v-text-field>
                <v-spacer></v-spacer>
                    <v-icon class="mr-10 mt-6"
                        @click="createItem" > person_add
                    </v-icon>
            </v-card-title>
            <v-data-table
                :headers="headers"
                :items="users"
                :search="search"
                :items-per-page="5"
                class="elevation-1" >
                <template v-slot:item.action="{ item }">
                    <v-icon small class="mr-4"
                        @click="editItem(item)" > edit
                    </v-icon>
                    <v-icon small class="ml-4"
                        @click="deleteItem(item)" > delete
                    </v-icon>
                </template>
            </v-data-table>
        </v-card> `

});

Vue.component('place', {

    data: function() {
        return {
            place: null,
            switch1: false,
            valid: true,
            id: '',
            name: '',
            nameRules: [
                v => !!v || 'Name is required',
                v => (v && v.length <= 20) || 'Name must be less than 20 characters',
            ],
            numberOfPlace: '',
            numberOfRow: '',
            switchRules: [
                v => !!v || 'You must agree to continue!',
            ],
        }
    },

    mounted: function() {
        placeId = this.$route.params.id;
        if(placeId != 'newPlace') {
            placeApi.get({id: placeId}).then(result=> {
                result.json().then(data => {
                    this.place = data;
                })
            }, response => {
                console.log(response);
            })
        }
    },

    watch: {
        place: function(newVal, oldVal) {
            this.name = newVal.name;
            this.numberOfPlace = newVal.numberOfPlace;
            this.numberOfRow = newVal.numberOfRow;
            this.id = newVal.id;
        }
    },

    methods: {
        save: function() {
            if(this.$refs.form.validate()) {
                if(this.id) {
                    var placeToDB = {id: this.id, name: this.name, numberOfPlace: this.numberOfPlace,
                    numberOfRow: this.numberOfRow};
                    placeApi.update({id: this.id}, placeToDB).then(result => {
                        router.go(-1);
                    }, response => {
                        console.log(response);
                    })
                } else {
                    var placeToDB = {name: this.name, numberOfPlace: this.numberOfPlace,
                    numberOfRow: this.numberOfRow};
                    placeApi.save(placeToDB).then(result=> {
                        router.go(-1);
                    }, response => {
                        console.log(response);
                    })
                }
            }
        },
        clearForm: function() {
            this.name = '';
            this.numberOfPlace = '';
            this.numberOfRow = '';
        },
        backSpace: function() {
            router.go(-1);
        }
    },

    template:
        ` <v-layout class="ma-10">
            <v-form
                ref="form"
                v-model="valid"
                lazy-validation >
                <v-row>
                    <v-col cols="12" md="1">
                        <v-text-field
                            v-model="id"
                            label="ID"
                            disabled
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="name"
                            :counter="20"
                            :rules="nameRules"
                            label="Name"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="numberOfPlace"
                            :counter="20"
                            label="Number of Places"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="numberOfRow"
                            :counter="30"
                            label="Number of Rows"
                        ></v-text-field>
                    </v-col>
                </v-row>
                <v-switch
                    v-model="switch1"
                    label="Do you agree?"
                    :rules="switchRules"
                ></v-switch>
                <v-btn
                    :disabled="!valid "
                    color="success"
                    class="mr-4"
                    @click="save" >
                    Save
                </v-btn>
                <v-btn
                    color="lime"
                    class="mr-4"
                    @click="clearForm" >
                    Clear
                </v-btn>
                <v-btn
                    color="cyan"
                    class="mr-4"
                    @click="backSpace" >
                    Back
                </v-btn>
                </v-row>
            </v-form>
        </v-layout> `
});

Vue.component('places-list', {

    data: function() {
        if (isAdmin()) {
            return {
                places: [],
                search: '',
                addFunction: true,
                headers: [{
                    text: 'ID',
                    align: 'left',
                    sortable: false,
                    value: 'id',
                },
                { text: 'Name', value: 'name' },
                { text: 'Number of rows', value: 'numberOfRow' },
                { text: 'Number of places', value: 'numberOfPlace' },
                { text: 'Events', value: 'event', sortable: false },
                { text: 'Actions', value: 'action', sortable: false },
                ]
            }
        } else {
            return {
                places: [],
                search: '',
                addFunction: false,
                headers: [{
                    text: 'ID',
                    align: 'left',
                    sortable: false,
                    value: 'id',
                },
                { text: 'Name', value: 'name' },
                { text: 'Number of rows', value: 'numberOfRow' },
                { text: 'Number of places', value: 'numberOfPlace' },
                { text: 'Events', value: 'event', sortable: false },
                ]
            }
        }
    },

    mounted: function() {
        placeApi.get().then(result=> {
            result.json().then(data => {
                this.places = data;
            })
        }, response => {
            console.log(response);
        })
    },

    methods: {
        editItem: function(item){
            router.push({ path: `/place/${item.id}` })
        },

        deleteItem: function(item){
            placeId = item.id,
            placeApi.remove({id: placeId}).then(result=>{
                if(result.ok) {
                    this.places.splice(this.places.indexOf(item), 1)
                }
            }, response => {
                console.log(response);
            })
        },

        createItem: function(){
            router.push({ path: `/place/newPlace` })
        },

        giveEvents: function(item) {
            router.push({ path: `/show`, query:{ idPlace:item.id } })
        }
    },

    template:
        ` <v-card>
            <v-card-title>
                <v-layout>
                    Places
                </v-layout>
                <v-spacer></v-spacer>
                <v-text-field
                    v-model="search"
                    append-icon="search"
                    label="Search"
                    single-line
                    hide-details
                ></v-text-field>
                <v-spacer></v-spacer>
                    <v-icon v-if="addFunction"
                        class="mr-10 mt-6"
                        @click="createItem" > playlist_add
                    </v-icon>
            </v-card-title>
            <v-data-table
                :headers="headers"
                :items="places"
                :search="search"
                :items-per-page="5"
            >
                <template v-slot:item.event="{ item }">
                    <v-icon small class="mr-4"
                        @click="giveEvents(item)" > list
                    </v-icon>
                </template>
                <template v-slot:item.action="{ item }">
                    <v-icon small class="mr-4"
                        @click="editItem(item)" > edit
                    </v-icon>
                    <v-icon small class="ml-4"
                        @click="deleteItem(item)" > delete
                    </v-icon>
                </template>
            </v-data-table>
        </v-card> `

});

Vue.component('show', {

    data: function() {
        return {
            id: '',
            name: '',
            date: '',
            time: '',
            place: Object,
            places: [],
            nameRules: [
                v => !!v || 'Name is required',
                v => (v && v.length <= 20) || 'Name must be less than 20 characters',
            ],
            placeRules: [
                v => !!v || 'Name is required' ,
            ],
            menu: false,
            menu2: false,
            readonly: false,
            switchShow: false,
            switchShowRules: [
                v => !!v || 'You must agree to continue!',
            ],
            valid: true,
        }
    },

    mounted: function() {
        showId = this.$route.params.id;
        placeApi.get().then(result=> {
            result.json().then(data => {
                this.places = data;
            })
            if(showId != 'newShow') {
                showApi.get({id: showId}).then(result=> {
                    result.json().then(data => {
                        this.id = data.id;
                        this.name = data.name;
                        this.date = data.dateTime.substr(0,10);
                        this.time = data.dateTime.substr(11,16);
                        this.place = this.getPlaceFromArrPlaces(data.locationId);
                    })
                }, response => {
                    console.log(response);
                })
            }
        }, response => {
            console.log(response);
        })
    },

    methods: {
        getPlaceFromArrPlaces: function(locationId) {
            var place;
            this.places.forEach(function(item, i, arr) {
                if(item.id == locationId){
                    place = item;
                }
            });
            return place;
        },

        save: function() {
            if(this.$refs.form.validate()) {
                if(this.id) {
                    var showToDB = {id: this.id, name: this.name,
                        dateTime: this.date + ' ' + this.time, locationId: this.place.id};
                    showApi.update({id: this.id}, showToDB).then(result => {
                        router.go(-1);
                    }, response => {
                        console.log(response);
                    })
                } else {
                    var showToDB = {name: this.name, dateTime: this.date + ' ' + this.time,
                        locationId: this.place.id};
                    showApi.save(showToDB).then(result => {
                        router.go(-1);
                    }, response => {
                        console.log(response);
                    });
                }
            }
        },

        clearForm: function() {
            this.name = '';
            this.date = '';
            this.time = '';
            this.place = null;
        },

        backSpace: function() {
            router.go(-1);
        }
    },

    template:
        ` <v-layout class="ma-10">
            <v-form
                ref="form"
                v-model="valid"
                lazy-validation >
                <v-row>
                    <v-col cols="12" md="1">
                        <v-text-field
                            v-model="id"
                            label="ID"
                            disabled
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-text-field
                            v-model="name"
                            :counter="20"
                            :rules="nameRules"
                            label="Name"
                            required
                        ></v-text-field>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                        <v-menu
                            ref="menu"
                            v-model="menu"
                            :close-on-content-click="false"
                            :return-value.sync="date"
                            transition="scale-transition"
                            offset-y
                            min-width="290px" >
                            <template v-slot:activator="{ on }" >
                                <v-text-field
                                    v-model="date"
                                    label="Date"
                                    prepend-icon="event"
                                    readonly="readonly"
                                    v-on="on"
                                ></v-text-field>
                            </template>
                            <v-date-picker v-model="date" no-title scrollable>
                                <v-spacer></v-spacer>
                                <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
                                <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
                            </v-date-picker>
                        </v-menu>
                    </v-col>
                    <v-col cols="12" sm="6" md="4">
                        <v-menu
                            ref="menu2"
                            v-model="menu2"
                            :close-on-content-click="false"
                            :nudge-right="40"
                            :return-value.sync="time"
                            transition="scale-transition"
                            offset-y
                            max-width="290px"
                            min-width="290px" >
                            <template v-slot:activator="{ on }">
                                <v-text-field
                                    v-model="time"
                                    label="Time"
                                    prepend-icon="access_time"
                                    readonly
                                    v-on="on"
                                ></v-text-field>
                            </template>
                            <v-time-picker
                                v-if="menu2"
                                v-model="time"
                                full-width
                                @click:minute="$refs.menu2.save(time)"
                            ></v-time-picker>
                        </v-menu>
                    </v-col>
                    <v-col cols="12" md="4">
                        <v-select
                            v-model="place"
                            :items="places"
                            label="Place"
                            :rules="placeRules"
                            required
                            item-text="name"
                            item-value="place"
                            return-object
                        ></v-select>
                    </v-col>
                </v-row>
                <v-switch
                    v-model="switchShow"
                    label="Do you agree?"
                    :rules="switchShowRules"
                ></v-switch>
                <v-btn
                    :disabled="!valid "
                    color="success"
                    class="mr-4"
                    @click="save" >
                    Save
                </v-btn>
                <v-btn
                    color="lime"
                    class="mr-4"
                    @click="clearForm" >
                    Clear
                </v-btn>
                <v-btn
                    color="cyan"
                    class="mr-4"
                    @click="backSpace" >
                    Back
                </v-btn>
            </v-form>
        </v-layout> `
});

Vue.component('shows-list', {

    data: function() {
            if (isAdmin()) {
                return {
                    shows: [],
                    query: this.$route.query.idPlace,
                    idPlace: '',
                    idPlaceFromDB: '',
                    search: '',
                    addFunction: true,
                    headers: [{
                        text: 'ID',
                        align: 'left',
                        sortable: false,
                        value: 'id',
                    },
                    { text: 'Name', value: 'name' },
                    { text: 'Date and Time', value: 'dateTime' },
                    { text: 'Place', value: 'location.name' },
                    { text: 'Tickets', value: 'ticket', sortable: false },
                    { text: 'Actions', value: 'action', sortable: false },
                    ]
                }
            } else {
                return {
                    shows: [],
                    query: this.$route.query.idPlace,
                    idPlace: '',
                    idPlaceFromDB: '',
                    search: '',
                    addFunction: false,
                    headers: [{
                        text: 'ID',
                        align: 'left',
                        sortable: false,
                        value: 'id',
                    },
                    { text: 'Name', value: 'name' },
                    { text: 'Date and Time', value: 'dateTime' },
                    { text: 'Place', value: 'location.name' },
                    { text: 'Tickets', value: 'ticket', sortable: false },
                    ]
                }
            }
        },

    mounted: function() {
        this.getEvents();
    },

    watch: {
        $route(to, from) {
            this.getEvents();
        }
    },

    methods: {
        getEvents: function() {
            this.$http.get('/event', {params: {locationId: this.$route.query.idPlace}}).then(result=> {
                result.json().then(data => {
                    this.shows = data;
                })
            }, response => {
                console.log(response);
            })
        },

        editItem: function(item){
            showId = item.id,
            router.push({ path: `/show/${showId}` })
        },

        deleteItem: function(item){
            showId = item.id,
            showApi.remove({id: showId}).then(result=>{
                if(result.ok) {
                    this.shows.splice(this.shows.indexOf(item), 1);
                }
            }, response => {
                console.log(response);
            })
        },

        createItem: function(){
            router.push({ path: `/show/newShow` })
        },

        giveTickets: function(item) {
            router.push({ path: `/card`, query:{ idShow:item.id } })
        }
    },

    template:
        ` <v-card>
            <v-card-title>
                <v-layout>
                    Shows
                </v-layout>
                <v-spacer></v-spacer>
                <v-text-field
                    v-model="search"
                    append-icon="search"
                    label="Search"
                    single-line
                    hide-details
                ></v-text-field>
                <v-spacer></v-spacer>
                    <v-icon v-if="addFunction"
                        class="mr-10 mt-6"
                        @click="createItem" > playlist_add
                    </v-icon>
            </v-card-title>
            <v-data-table
                :headers="headers"
                :items="shows"
                :search="search"
                :items-per-page="5"
                class="elevation-1" >
                <template v-slot:item.ticket="{ item }">
                    <v-icon small class="mr-4"
                        @click="giveTickets(item)" > list
                    </v-icon>
                </template>
                <template v-slot:item.action="{ item }">
                    <v-icon small class="mr-4"
                        @click="editItem(item)" > edit
                    </v-icon>
                    <v-icon small class="ml-4"
                        @click="deleteItem(item)" > delete
                    </v-icon>
                </template>
            </v-data-table>
        </v-card> `
});

Vue.component('cards-list', {

    data: function() {
        return {
            nRow: Number,
            kCol: Number,
            placeId: Number,
            showId: Number,
            authUserId: Number,
            cards: [],
            cardsToBuy: [],
            defaultColor: 'blue lighten-5',
            colorScene: 'green',
            disabledCard: false,
            visibleBtnBuyTicket: false,
            dialogOk: false,
            dialogError: false,
            width: 1000,
            height: 80,
        }
    },

    mounted: function() {
        this.$http.get('/ticket', {params: {eventId: this.$route.query.idShow} }).then(resultTickets => {
            resultTickets.json().then(dataTickets => {
                this.cards = dataTickets;
            })
        }, responseTickets => {
            console.log(responseTickets);
        });
        showApi.get({id: this.$route.query.idShow}).then(resultShow => {
            resultShow.json().then(dataShow => {
                this.showId = dataShow.id;
                this.placeId = dataShow.locationId;
                placeApi.get({id: this.placeId}).then(resultPlace => {
                    resultPlace.json().then(dataPlace => {
                        this.nRow = dataPlace.numberOfRow;
                        this.kCol = dataPlace.numberOfPlace;
                    })
                },responsePlace => {
                    console.log(responsePlace);
                })
            })
        }, responseShow => {
           console.log(responseShow);
        });
        authUserApi.get().then(resultAuthUser => {
            resultAuthUser.json().then(dataAuthUser => {
                this.authUserId = dataAuthUser.id;
            })
        },responseAuthUser => {
            console.log(responseAuthUser);
        })
    },

    watch: {
        cardsToBuy: function() {
            this.visibleBtnBuyTicket = this.cardsToBuy.length>0
        }
    },

    methods: {
        buyCards: function() {
            cardApi.save(this.cardsToBuy).then(result => {
                this.dialogOk = true;
            }, response => {
                this.dialogError = true;
            })
        },

        turnBack: function() {
            router.go(-1);
        }
    },

    template:
        ` <div>
            <v-dialog
                v-model="dialogOk"
                max-width="290"
            >
                <v-card>
                    <v-card-title class="headline">Success</v-card-title>
                    <v-card-text>
                        Сongratulations, You buy {{this.cardsToBuy.length}} ticket(s).
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                            color="green darken-1"
                            text
                            @click="turnBack"
                        >
                            Ok
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
            <v-dialog
                v-model="dialogError"
                max-width="290"
            >
                <v-card>
                    <v-card-title class="headline">Error</v-card-title>
                    <v-card-text>
                        An error has occurred, try again later.
                    </v-card-text>
                    <v-card-actions>
                        <v-spacer></v-spacer>
                        <v-btn
                            color="green darken-1"
                            text
                            @click="turnBack"
                        >
                            Ok
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
            <v-card
                style="margin-bottom: 20px"
            >
                <v-navigation-drawer absolute>
                    <v-list-item>
                        <v-list-item-content>
                            <v-list-item-title class="title">
                                Tickets
                            </v-list-item-title>
                            <v-list-item-subtitle>
                                to buy
                            </v-list-item-subtitle>
                        </v-list-item-content>
                    </v-list-item>
                    <v-divider></v-divider>
                    <v-list>
                        <v-list-item
                            v-for="card in cardsToBuy"
                            :key="card.place + ' ' + card.row"
                        >
                            Row: {{card.row}}    Place: {{card.place}}
                        </v-list-item>
                    </v-list>
                    <v-btn
                        v-if="visibleBtnBuyTicket"
                        class="ma-2"
                        color="green"
                        @click="buyCards"
                    >
                        Buy
                    </v-btn>
                    <v-btn
                        color="cyan"
                        class="mr-4"
                        @click="turnBack"
                    >
                        Back
                    </v-btn>
                </v-navigation-drawer>
                <v-main class="pt-2 pb-4" style="margin-left: 256px" >
                    <v-row>
                        <v-sheet
                            class="pa-md-4 mb-4 mx-lg-auto"
                            :width="width"
                            :height="height"
                            :color="colorScene"
                            >
                            <v-card-text>
                                <p class="text-center">Scene</p>
                            </v-card-text>
                        </v-sheet>
                    </v-row>
                    <v-row justify="center"
                        v-for="row in nRow"
                        :key="row"
                        no-gutters>
                        <cardInCardList
                            v-for="column in kCol" :key="column"
                            :column="column" :row="row"
                            :placeId="placeId" :showId="showId" :authUserId="authUserId"
                            :cards="cards" :cardsToBuy="cardsToBuy"
                            >
                        </cardInCardList>
                    </v-row>
                </v-main>
            </v-card>
        </div> `
});

Vue.component('cardInCardList', {
    props: {
        placeId: Number,
        showId: Number,
        authUserId: Number,
        column: Number,
        row: Number,
        cards: Array,
        cardsToBuy: Array,
    },

    data: function() {
        return {
            card: {
                eventId: this.showId ,
                locationId: this.placeId ,
                userId: Number ,
                place: this.column ,
                row: this.row ,
                ticketStatus: 'NOT_PURCHASED'
            },
            colorFontIcon: '#F4FF81',
            disabledIcon: false

        }
    },

    mounted: function() {
        for (var i=0; i<this.cards.length; i++) {
            if (this.cards[i].place == this.column && this.cards[i].row == this.row) {
                this.card = this.cards[i];
                if (this.card.ticketStatus == 'PURCHASED') {
                    this.colorFontIcon = '#E3F2FD';
                    this.disabledIcon = true;
                };
                if (this.card.ticketStatus == 'RESERVED') {
                    this.colorFontIcon = '#FFEB3B';
                    this.disabledIcon = true;
                };
            }
        }

    },

    methods: {
        getCard: function() {
            this.card.userId = this.authUserId;
            this.card.ticketStatus = 'PURCHASED';
            for (var i=0; i<this.cardsToBuy.length; i++) {
                if (this.cardsToBuy[i].place == this.card.place
                    && this.cardsToBuy[i].row == this.card.row) {
                        this.cardsToBuy.splice(this.cardsToBuy.indexOf(this.cardsToBuy[i]), 1);
                        this.colorFontIcon = '#F4FF81';
                        return;
                    }
            }
            this.cardsToBuy.push(this.card);
            this.colorFontIcon = '#FAFAFA';
        }
    },

    template:
        ` <v-icon
            class="pa-1"
            :style="{ 'background-color': colorFontIcon  }"
            :disabled="disabledIcon"
            @click="getCard()"
            >
            crop_square
        </v-icon> `
});

Vue.component('profile', {
    data: function() {
        return {
            user: Object,
            cards: Array,
            shows: Array,
            places: Array,
            cardWidth: 400
        }
    },

    mounted: function() {
        this.$http.get('/auth/user').then(resultAuthUser => {
            resultAuthUser.json().then(dataAuthUser => {
                this.user = dataAuthUser;
                this.$http.get('/ticket', {params: {userId: this.user.id} }).then(resultTickets => {
                    resultTickets.json().then(dataTickets => {
                        this.cards = dataTickets;
                    })
                }, responseTickets => {
                    console.log(responseTickets);
                })
            })
        }, responseAuthUser => {
            console.log(responseAuthUser);
        });
        showApi.get().then(result=> {
            result.json().then(data => {
                this.shows = data;
            })
        }, response => {
            console.log(response);
        });
        placeApi.get().then(result=> {
            result.json().then(data => {
                this.places = data;
            })
        }, response => {
            console.log(response);
        })
    },

    methods: {
        turnBack: function() {
            router.go(-1);
        }
    },

    filters: {
        getEntityName: function(idEntity, arrayEntity) {
            for(var i=0; i<arrayEntity.length; i++) {
                if(idEntity == arrayEntity[i].id) {
                    return arrayEntity[i].name;
                }
            }
        },
    },

    template:
        ` <v-container>
            <v-card>
                <v-container>
                    <v-row
                        class="mx-auto"
                    >
                        <v-col>
                            <v-text-field
                                :value="user.login"
                                label="Login"
                                outlined
                                readonly
                                class="mx-auto my-3"
                            ></v-text-field>
                            <v-text-field
                                :value="user.firstName"
                                label="First Name"
                                outlined
                                readonly
                                class="mx-auto my-3"
                            ></v-text-field>
                            <v-text-field
                                :value="user.lastName"
                                label="Last Name"
                                outlined
                                readonly
                                class="mx-auto my-3"
                            ></v-text-field>
                        </v-col>
                        <v-col>
                            <v-row
                                v-for="(card, i) in cards"
                                :key="i"
                            >
                                <v-card
                                    class="mx-auto my-2 pa-2"
                                    color="pink darken-2"
                                    dark
                                    width="300"
                                >
                                    <v-text-field
                                        :value="card.locationId | getEntityName(places)"
                                        label="Location"
                                        outlined
                                        readonly
                                    ></v-text-field>
                                    <v-text-field
                                        :value="card.eventId | getEntityName(shows)"
                                        label="Event"
                                        outlined
                                        readonly
                                    ></v-text-field>
                                    <v-text-field
                                        :value="card.row"
                                        label="Row"
                                        outlined
                                        readonly
                                    ></v-text-field>
                                    <v-text-field
                                        :value="card.place"
                                        label="Place"
                                        outlined
                                        readonly
                                    ></v-text-field>
                                </v-card>
                            </v-row>
                        </v-col>
                    </v-row>
                </v-container>
                <v-container>
                    <v-btn
                        color="cyan"
                        class="mr-4"
                        @click="turnBack"
                    >
                        Back
                    </v-btn>
                </v-container>
            </v-card>
        </v-container> `
});

const Registration = {
    template:
        ` <registration></registration> `
}

const Home = {
    template:
        ` <div> Home </div> `
}

const User = {
    template:
        ` <user></user> `
}

const Users = {
    template:
        ` <users-list></users-list> `
}

const Place = {
    template:
        ` <place></place> `
}

const Places = {
    template:
        ` <places-list></places-list> `
}

const Show = {
    template:
        ` <show></show> `
}

const Shows = {
    template:
        ` <shows-list></shows-list> `
}

const Cards = {
    template:
        ` <cards-list></cards-list> `
}

const Profile = {
    template:
        ` <profile></profile> `
}

const router = new VueRouter({
    mode: 'history',
    routes: [
        { path: '/registration', component: Registration},
        { path: '/person/:id', component: User },
        { path: '/person', component: Users, },
        { path: '/place/:id', component: Place },
        { path: '/place', component: Places },
        { path: '/show/:id', component: Show },
//        { path: '/show', component: Shows, props: (route) => ({query:route.query}) },
        { path: '/show', component: Shows },
//        { path: '/card', component: Cards, props: (route) => ({query:route.query}) },
        { path: '/card', component: Cards },
//        { path: '/profile', component: Profile, props: {profile: frontEndData.profile}},
        { path: '/profile', component: Profile },
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
            return isAdmin()
        }
    },
})