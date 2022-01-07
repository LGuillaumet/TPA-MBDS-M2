import { Container, Row, Col, Card, CardGroup, CardImg, CardText, CardBody, CardTitle, Button, CardFooter, ListGroupItem, ListGroup } from 'reactstrap';

import d3Logo from '../assets/D3.png';
import reactLogo from '../assets/ReactLogo.png';
import rechartsLogo from '../assets/RechartsLogo.png';
import gitHubLogo from '../assets/logoGitHub.png';


import { NavBar } from '../components/NavBar';

import './Home.scss';

export const Home = () => {
	return (
		<>
			<NavBar />
			<Container className="HomePage">
				<Row className="justify-content-center">
					<Col xs="12" sm="12" md="12" lg="12" xl="12">
						<Card className="home-card my-3">
							<Row className="justify-content-center">
								<Col xs="12" sm="12" md="12" lg="12" xl="12">
									<h1>TPA MIAGE M2</h1>
								</Col>
							</Row>
						</Card>
						<Row className="justify-content-center">
							<Col xs="12" sm="12" md="12" lg="12" xl="12">
								<h3>Bienvenue sur la data visualization de notre projet</h3>
								<p>Le groupe est composé des membres suivants :</p>
								<ListGroup className="ListContributors">
									<ListGroupItem
										action
										href="https://github.com/StevenBouche"
										target="_blank"
										tag="a"
									>
										Steven Bouche
									</ListGroupItem>
									<ListGroupItem
										action
										href="https://github.com/ArmandPrevot"
										target="_blank"
										tag="a"
									>
										Armand Prevot
									</ListGroupItem>
									<ListGroupItem
										action
										href="https://github.com/CamillePERESe"
										target="_blank"
										tag="a"
									>
										Camille Peres
									</ListGroupItem>
									<ListGroupItem
										action
										href="https://github.com/Dorian-Chapoulie"
										target="_blank"
										tag="a"
									>
										Dorian Chapoulié
									</ListGroupItem>
									<ListGroupItem
										action
										href="https://github.com/LGuillaumet"
										target="_blank"
										tag="a"
									>
										Léo Guillaumet
									</ListGroupItem>
								</ListGroup>

							</Col>
						</Row>
						<Row xs="auto" className="justify-content-center align-items-center mt-3">
							<Col xs="auto">
								<Button color="secondary" href="/#/dataviz" target="">
									Aller vers les visualisations
								</Button>
							</Col>
						</Row>
						<Row className="justify-content-center mt-3">
							<Col xs="12" sm="12" md="12" lg="12" xl="12">
								<h4>Technologies utilisées</h4>
								<CardGroup className="Techs justify-content-center">
									<Card className="">
										<CardImg
											alt="D3Logo"
											src={d3Logo}
											top
											width="100%"
										/>
										<CardBody>
											<CardTitle tag="h5">
												D3.js
											</CardTitle>
											<CardText>
												D3.js est une bibliothèque JavaScript permettant de manipuler des documents à partir de données. D3 vous aide à donner vie à vos données en utilisant HTML, SVG et CSS.
											</CardText>
										</CardBody>
										<CardFooter>
											<Button color="info" href="https://d3js.org/" target="_blank">
												Plus d'informations
											</Button>
										</CardFooter>
									</Card>
									<Card>
										<CardImg
											alt="Card image cap"
											src={reactLogo}
											top
											width="100%"
										/>
										<CardBody>
											<CardTitle tag="h5">
												React
											</CardTitle>
											<CardText>
												React est une bibliothèque JavaScript libre développée par Facebook depuis 2013. Le but principal de cette bibliothèque est de faciliter la création d'application web monopage, via la création de composants dépendant d'un état et générant une page HTML à chaque changement d'état.											</CardText>
										</CardBody>
										<CardFooter>
											<Button color="info">
												Plus d'informations
											</Button>
										</CardFooter>
									</Card>
									<Card>
										<CardImg
											alt="Card image cap"
											src={rechartsLogo}
											top
											width="100%"
										/>
										<CardBody>
											<CardTitle tag="h5" href="https://recharts.org/en-US/" target="_blank">
												Recharts
											</CardTitle>

											<CardText>
												Recharts est une bibliothèque de graphiques redéfinis construite avec React et D3.
												L'objectif principal de cette bibliothèque est d'aider à écrire des graphiques dans des applications React sans aucune difficulté.											</CardText>

										</CardBody>
										<CardFooter>
											<Button color="info" href="https://fr.reactjs.org/" target="_blank">
												Plus d'informations
											</Button>
										</CardFooter>
									</Card>
								</CardGroup>
							</Col>
						</Row>

						<Row xs="auto" className="justify-content-center align-items-center mt-5">
							<Col xs="auto">
								<CardImg
									alt="Card image cap"
									src={gitHubLogo}
									top
									width="100%"
								/>
							</Col>
							<Col xs="auto">
								<Button color="secondary" href="https://github.com/StevenBouche/TPA-MBDS-M2" target="_blank">
									Aller vers le repo git
								</Button>
							</Col>
						</Row>
					</Col>
				</Row>
			</Container >

		</>
	);
}
