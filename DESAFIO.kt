import java.time.LocalDateTime

fun main () {
    val digitalHouse = DigitalHouseManager()
    digitalHouse.registrarCurso("Mobile", 1234, 40)
    digitalHouse.registrarCurso("IOS", 1010, 20)
    digitalHouse.excluirCurso(1010)
    digitalHouse.excluirCurso(1011)
    digitalHouse.registrarProfessorAdjunto("Bruno", "Marques", 1478, 20, 0)
    digitalHouse.registrarProfessorTitular("Mateus", "Cardoso", 2587, "Francês", 0)
    digitalHouse.registrarProfessorTitular("Pedro", "Freira", 1236, "Matemática", 0)
    digitalHouse.excluirProfessor(1236)
    digitalHouse.excluirProfessor(1231)
    digitalHouse.registrarAluno("Pollyana", "Minatel", 1001)
    digitalHouse.matricularAluno(1001, 1234)
    digitalHouse.matricularAluno(1047, 1234)
    digitalHouse.matricularAluno(1001, 1254)
    digitalHouse.alocarProfessores(1234, 2587, 1478)
    digitalHouse.alocarProfessores(1111, 2587, 1478)
    digitalHouse.alocarProfessores(1234, 1111, 1478)
    digitalHouse.alocarProfessores(1234, 2587, 1111)
}

interface Pessoa{
    var nome: String
    var sobrenome: String
    var codigo : Int
}

class Aluno (override var nome: String, override var sobrenome: String, override var codigo: Int) : Pessoa

open class Professor (override var nome: String, override var sobrenome: String, override var codigo: Int, open var tempoCasa: Int) : Pessoa

class ProfessorTitular (override var nome: String, override var sobrenome: String, override var codigo: Int, var especialidade: String, override var tempoCasa: Int) : Professor(nome, sobrenome, codigo, tempoCasa)

class ProfessorAdjunto(override var nome: String, override var sobrenome: String, override var codigo: Int, var horasMonitoria: Int, override var tempoCasa: Int) : Professor(nome, sobrenome, codigo, tempoCasa)

class Curso (var curso: String, var codigo: Int, var maxAlunos: Int) {
    var professorTitular: Professor? = null
    var professorAdjunto: Professor? = null
    var listaAlunos = mutableListOf<Aluno>()

    fun adicionarAluno (aluno: Aluno?): Boolean {
        if (this.maxAlunos > this.listaAlunos.size) {
            aluno?.let {
                listaAlunos.add(it)
                return true
            }
        }
        return false
    }

    fun excluirAluno(aluno: Aluno) {
        listaAlunos.remove(aluno)
    }
}

class Matricula (var aluno: Aluno, var curso: Curso, var dataMatricula: LocalDateTime = LocalDateTime.now())

class DigitalHouseManager {
    var listaAlunos = mutableListOf<Aluno>()
    var listaProfessores = mutableListOf<Professor>()
    var listaCursos = mutableListOf<Curso>()
    var listaMatriculas = mutableListOf<Matricula>()

    fun registrarCurso(nome: String, codigo: Int, maxAlunos: Int) {
        val curso = Curso(nome, codigo, maxAlunos)
        listaCursos.add(curso)
        println("O curso foi adicionado à lista")
    }

    fun excluirCurso(codigo: Int) {
        var cursoExiste = false
        for (curso in listaCursos) {
            if (curso.codigo == codigo) {
                cursoExiste = true
                listaCursos.remove(curso)
                println("O curso ${curso.codigo} foi excluído com sucesso")
                break
            }
        }
        if (!cursoExiste){
            println("O curso $codigo não existe")
        }
    }

    fun registrarProfessorAdjunto(nome: String, sobrenome: String, codigo: Int, horasDisponiveis: Int, tempoCasa: Int) {
        val professor = ProfessorAdjunto(nome, sobrenome, codigo, horasDisponiveis, tempoCasa)
        listaProfessores.add(professor)
        println("O professor foi adicionado à lista")
    }

    fun registrarProfessorTitular(nome: String, sobrenome: String, codigo: Int, especialidade: String, tempoCasa: Int) {
        val professor = ProfessorTitular(nome, sobrenome, codigo, especialidade, tempoCasa)
        listaProfessores.add(professor)
        println("O professor foi adicionado à lista")
    }

    fun excluirProfessor(codigo: Int) {
        var professorExiste = false
        for (professor in listaProfessores) {
            if (professor.codigo == codigo){
                professorExiste = true
                listaProfessores.remove(professor)
                println("O professor foi excluído da lista")
                break
            }
        }
        if (!professorExiste){
            println("O professor $codigo não existe")
        }
    }

    fun registrarAluno(nome: String, sobrenome: String, codigo: Int){
        val aluno = Aluno(nome, sobrenome, codigo)
        listaAlunos.add(aluno)
        println("O aluno foi registrado e adicionado à lista")
    }

    fun matricularAluno(codigoAluno: Int, codigoCurso: Int){
        var aluno: Aluno? = null
        var alunoExiste = false
        var cursoExiste = false
        for (alunoDaLista in listaAlunos) {
            if (alunoDaLista.codigo == codigoAluno) {
                aluno = alunoDaLista
                alunoExiste = true
                break
            }
        }
        if (!alunoExiste){
            println("O aluno $codigoAluno não existe")
            return
        }
        for (curso in listaCursos) {
            if (curso.codigo == codigoCurso) {
                curso.adicionarAluno(aluno)
                cursoExiste = true
                if (curso.adicionarAluno(aluno)) {
                    val matricula = aluno?.let { it1 -> Matricula(it1, curso) }
                    if (matricula != null) {
                        listaMatriculas.add(matricula)
                        println("Matricula realizada com sucesso!")
                        break
                    }
                }
            }
        }
        if (!cursoExiste){
            println("O curso $codigoCurso não existe")
        }
    }

    fun alocarProfessores(codigoCurso: Int, codigoProfessorTitular: Int, codigoProfessorAdjunto: Int) {
        var professorTitular : Professor? = null
        var professorAdjunto : Professor? = null
        var professorTitularExiste = false
        var professorAdjuntoExiste = false
        var cursoExiste = false


        for (professor in listaProfessores) {
            if (professor.codigo == codigoProfessorAdjunto) {
                professorAdjunto = professor
                professorAdjuntoExiste = true
            } else if (professor.codigo == codigoProfessorTitular) {
                professorTitular = professor
                professorTitularExiste = true
            }
            if (professorAdjuntoExiste && professorTitularExiste){
                break
            }
        }


        if (!professorAdjuntoExiste) {
            println("O professor adjunto $codigoProfessorAdjunto não existe")
        }
        if (!professorTitularExiste) {
            println("O professor titular $codigoProfessorTitular não existe")
        }

        for (curso in listaCursos) {
            if (curso.codigo == codigoCurso) {
                cursoExiste = true
                if (professorAdjuntoExiste && professorTitularExiste) {
                    curso.professorAdjunto = professorAdjunto
                    curso.professorTitular = professorTitular
                    println("Os professores foram alocados com sucesso")
                    break
                }
            }
        }

        if (!cursoExiste) {
            println("O curso $codigoCurso não existe")
        }
    }
}




