import { Container, Row, Col, Card, CardGroup, CardImg, CardText, CardBody, CardTitle, Button, CardSubtitle } from 'reactstrap';

import d3Logo from '../assets/D3.png';
import reactLogo from '../assets/ReactLogo.png';
import rechartsLogo from '../assets/RechartsLogo.png';


import { NavBar } from '../components/NavBar';


export const Home = () => {
	return (
		<>
			<NavBar />
			<Container>
				<Row className="justify-content-center">
					<Col xs="12" sm="12" md="12" lg="12" xl="12">
						<Card className="home-card">
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
								<ul className="list-group d-flex m-auto">
									<li className="list-group-item">Steven Bouche</li>
									<li className="list-group-item">Armand Prévot</li>
									<li className="list-group-item">Camille Peres</li>
									<li className="list-group-item">Dorian Chapoulié</li>
									<li className="list-group-item">Léo Guillaumet</li>
								</ul>
							</Col>
						</Row>
						<Row className="justify-content-center">
							<Col xs="12" sm="12" md="12" lg="12" xl="12">
								<h4>Technologies utilisées</h4>
								<p>Le groupe est composé des membres suivants :</p>
								<CardGroup>
									<Card>
										<CardImg
											alt="Card image cap"
											src={d3Logo}
											top
											width="100%"
										/>
										<CardBody>
											<CardTitle tag="h5">
												Card title
											</CardTitle>
											<CardSubtitle
												className="mb-2 text-muted"
												tag="h6"
											>
												Card subtitle
											</CardSubtitle>
											<CardText>
												This is a wider card with supporting text below as a natural lead-in to additional content. This content is a little bit longer.
											</CardText>
											<Button>
												Button
											</Button>
										</CardBody>
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
												Card title
											</CardTitle>
											<CardSubtitle
												className="mb-2 text-muted"
												tag="h6"
											>
												Card subtitle
											</CardSubtitle>
											<CardText>
												This card has supporting text below as a natural lead-in to additional content.
											</CardText>
											<Button>
												Button
											</Button>
										</CardBody>
									</Card>
									<Card>
										<CardImg
											alt="Card image cap"
											src={rechartsLogo}
											top
											width="100%"
										/>
										<CardBody>
											<CardTitle tag="h5">
												Card title
											</CardTitle>
											<CardSubtitle
												className="mb-2 text-muted"
												tag="h6"
											>
												Card subtitle
											</CardSubtitle>
											<CardText>
												This is a wider card with supporting text below as a natural lead-in to additional content. This card has even longer content than the first to show that equal height action.
											</CardText>
											<Button>
												Button
											</Button>
										</CardBody>
									</Card>
								</CardGroup>
							</Col>
						</Row>
					</Col>
				</Row>
			</Container >

		</>
	);
}
