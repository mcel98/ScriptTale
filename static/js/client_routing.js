const router = async () =>{
    const routes =[
        {path: "/main.html", view: () => console.log("home")},
        {path: "/docs.html", view: () => console.log("docs")},
        {path: "/contact.html", view: () => console.log("contact")}


    ];
    const matches = routes.map(route =>{

        return {

            route: route,
            isMatch: location.pathname === route.path

        };


    });

    let match = matches.find(matches => matches.isMatch );
    if(!match){

        match = {

            route: routes[0],
            isMatch: true
        };

    }
    console.log(match.route.view())
};

document.addEventListener("DOMContentLoaded", () => { router();});
